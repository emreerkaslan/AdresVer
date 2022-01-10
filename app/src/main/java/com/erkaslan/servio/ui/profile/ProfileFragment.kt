package com.erkaslan.servio.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.erkaslan.servio.databinding.FragmentProfileBinding
import com.erkaslan.servio.model.*
import dagger.hilt.android.AndroidEntryPoint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.commit
import com.erkaslan.servio.AllUtil
import com.erkaslan.servio.MainActivity
import com.erkaslan.servio.ui.home.MyServicesFragment
import com.google.gson.Gson
import com.shivtechs.maplocationpicker.LocationPickerActivity
import com.shivtechs.maplocationpicker.MapUtility
import org.json.JSONObject
import java.lang.Exception
import java.lang.StringBuilder
import android.content.Intent.createChooser
import android.app.Activity.RESULT_OK
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.test.core.app.ApplicationProvider
import com.erkaslan.servio.ui.home.MyEventsFragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    val prefs = activity?.getSharedPreferences((activity as MainActivity).PREFS_NAME, MODE_PRIVATE)
    val editor = prefs?.edit()
    private var util = AllUtil()
    private var username: String = "default"
    val ADDRESS_PICKER_REQUEST: Int = 0
    val PICK_IMAGE = 1
    var photoID: String? = null
    var imageUri: Uri? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        initViews()
        initObservers()
    }

    fun initViews() {
        //var isLoggedIn: Boolean =  prefs?.getBoolean("isLoggedIn", false) ?: false
        var isLoggedIn: Boolean = (activity as MainActivity).isLoggedIn
        binding.isLoggedin = (activity as MainActivity).isLoggedIn
        if (isLoggedIn != null && !isLoggedIn) {

            //Sets login buton click
            binding.btnLogin.setOnClickListener {
                val map = HashMap<String, String>()
                map.put("username", binding.etUsername.text.toString())
                map.put("password", binding.etPassword.text.toString())
                username = binding.etUsername.text.toString()
                profileViewModel.login(map)
                prefs?.edit()?.putString("username", binding.etUsername.text.toString())?.apply()
            }

            //Sets signup buton click
            binding.btnSignup.setOnClickListener {
                val map = HashMap<String, String>()
                map.put("username", binding.etUsernameRegister.text.toString())
                map.put("name", binding.etNameRegister.text.toString())
                map.put("password", binding.etPasswordRegister.text.toString())
                map.put("email", binding.etEmailRegister.text.toString())
                map.put("geolocation", binding.etGeolocationRegister.text.toString())
                map.put("bio", binding.etBioRegister.text.toString())
                map.put("interest", binding.etInterestRegister.text.toString())
                map.put("competency", binding.etCompetencyRegister.text.toString())
                if(photoID!=null){
                    uploadImage()
                    map.put("profilePicture", photoID as String)
                }
                val gson = Gson()
                val json = JSONObject(gson.toJson(map))
                profileViewModel.signup(json)
            }

            binding.tvMapRegister.setOnClickListener {
                val i = Intent(activity, LocationPickerActivity::class.java)
                startActivityForResult(i, ADDRESS_PICKER_REQUEST)
            }

            binding.tvPickImage.setOnClickListener {
                chooseImage();
            }
        } else {
            binding.currentUser = (activity as MainActivity).currentUser

            //Sets logout button click
            binding.tvLogout.setOnClickListener {
                profileViewModel.logout()
                val id = this.id
                fragmentManager?.commit {
                    detach(this@ProfileFragment)
                    replace(id, ProfileFragment())
                }
            }

            binding.btnServices.setOnClickListener {
                val id = this.id
                fragmentManager?.commit {
                    detach(this@ProfileFragment)
                    replace(id, MyServicesFragment(true, 0))
                }
            }

            binding.btnEvents.setOnClickListener {
                val id = this.id
                fragmentManager?.commit {
                    detach(this@ProfileFragment)
                    replace(id, MyEventsFragment(true, 0))
                }
            }
        }
    }

    fun initObservers() {
        profileViewModel.authTokenMutableLiveData?.observe(viewLifecycleOwner, {
            when(it){
                is GenericResult.Success -> {
                    (activity as MainActivity).isLoggedIn = true
                    binding.isLoggedin = true
                    it.data.let { token ->
                        prefs?.edit()?.putString("token", it.toString())?.commit()
                        (activity as MainActivity).token = token
                    }
                    profileViewModel.loginCheck(it.data.token, username)
                }
                is GenericResult.Failure -> {
                    binding.isLoggedin = false
                }
                else -> {
                    (activity as MainActivity).currentUser = null
                    (activity as MainActivity).isLoggedIn = false
                    (activity as MainActivity).token = null
                    binding.isLoggedin = false
                }
            }


        })

        profileViewModel.currentUserMutableLiveData?.observe(viewLifecycleOwner, {
            when(it){
                is GenericResult.Success -> {
                    Toast.makeText(context, "Logged in successfully! Enjoy Servio", Toast.LENGTH_LONG).show()
                    prefs?.edit()?.putString("username", it.data.username)?.commit()
                    binding.currentUser = it.data
                    (activity as MainActivity).currentUser = it.data
                    initViews()
                    Log.d("EX", "currentUser: " + it.data.toString())
                    profileViewModel.checkCredits((activity as MainActivity).token?.token ?: "", it.data.pk)
                }
                is GenericResult.Failure -> {
                    Toast.makeText(context, "Something is wrong, try singup again", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        })

        profileViewModel.serviceListMutableLiveData?.observe(viewLifecycleOwner, {
            when (it) {
                is GenericResult.Success -> {
                    var excessCredits = 0
                    it.data.forEach { excessCredits = excessCredits + it.credits }
                    if (excessCredits > 0) {
                        profileViewModel.addCredits(
                            (activity as MainActivity).token?.token ?: "",
                            (activity as MainActivity).currentUser?.pk ?: 0,
                            excessCredits
                        )
                    }
                }
                is GenericResult.Failure -> {
                    Toast.makeText(context, "Something is wrong, try singup again", Toast.LENGTH_LONG).show()
                }
                else -> {
                    (activity as MainActivity).currentUser = null
                    (activity as MainActivity).isLoggedIn = false
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    val currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0)
                    val currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0)
                    val completeAddress = data.getBundleExtra("fullAddress")
                    /* data in completeAddress bundle
                    "fulladdress"
                    "city"
                    "state"
                    "postalcode"
                    "country"
                    "addressline1"
                    "addressline2"
                     */
                    val addres = StringBuilder().append("addressline2: ").append(
                            completeAddress!!.getString("addressline2")
                        ).append("\ncity: ").append(
                            completeAddress.getString("city")
                        ).append("\npostalcode: ").append(
                            completeAddress.getString("postalcode")
                        ).append("\nstate: ").append(
                            completeAddress.getString("state")
                        ).toString()

                    val coordinates = StringBuilder().append("Lat:").append(currentLatitude).append("  Long:").append(currentLongitude).toString()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data?.data
            photoID = imageUri.toString()
        }
    }

    fun chooseImage() {
        val gallery = Intent()
        gallery.type = "image/*"
        gallery.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(createChooser(gallery, "Pick Image"), PICK_IMAGE)
    }

    private fun uploadImage() {
        photoID = UUID.randomUUID().toString()
        val storageReference: StorageReference = FirebaseStorage.getInstance().getReference().child("Servio/$photoID")
        val bos = ByteArrayOutputStream()
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(ApplicationProvider.getApplicationContext<Context>().getContentResolver(), imageUri)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)
            val data: ByteArray = bos.toByteArray()
            bitmap.recycle()
            val uploadTask: UploadTask = storageReference.putBytes(data)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}