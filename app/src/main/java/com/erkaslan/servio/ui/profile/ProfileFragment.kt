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
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import android.net.Uri
import com.erkaslan.servio.AllUtil
import com.erkaslan.servio.R
import com.google.gson.Gson
import java.lang.Exception


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private var sharedPreferences: SharedPreferences? = context?.getSharedPreferences("app", MODE_PRIVATE)
    private var util = AllUtil()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        initViews()
        initObservers()
    }

    fun initViews() {
        var isLoggedIn: Boolean =  sharedPreferences?.getBoolean("isLoggedIn", false) ?: false
        binding.isLoggedin = isLoggedIn
        if (!isLoggedIn) {
            binding.btnLogin.setOnClickListener {
                val map = HashMap<String, String>()
                map.put("username", binding.etUsername.text.toString())
                map.put("password", binding.etPassword.text.toString())
                profileViewModel.login(map)
                sharedPreferences?.edit()?.putString("username", binding.etUsername.text.toString())?.apply()
            }
            /*
            binding.btnSignup.setOnClickListener {
                val map = HashMap<String, String>()
                map.put("username", binding.etUsernameRegister.text.toString())
                map.put("name", binding.etNameRegister.text.toString())
                map.put("password", binding.etPasswordRegister.toString())
                map.put("email", binding.etEmailRegister.text.toString())
                map.put("geolocation", binding.etGeolocationRegister.text.toString())
                map.put("bio", binding.etBioRegister.text.toString())
                map.put("interest", binding.etInterestRegister.text.toString())
                map.put("competency", binding.etCompetencyRegister.text.toString())

                val validation = util.validateSignup(
                    binding.etUsernameRegister.text.toString(),
                    binding.etNameRegister.text.toString(),
                    binding.etPasswordRegister.toString(),
                    binding.etPasswordAgainRegister.text.toString(),
                    binding.etEmailRegister.text.toString(),
                    binding.etGeolocationRegister.text.toString(),
                    binding.etBioRegister.text.toString(),
                    binding.etInterestRegister.text.toString(),
                    binding.etCompetencyRegister.text.toString(),
                )
                if(validation.keys.first().equals(false)){
                    Toast.makeText(context, validation.get(key = false), Toast.LENGTH_LONG).show()
                } else {
                    profileViewModel.signup(map)
                }
            }

             */

        } else {
                sharedPreferences?.getString("currentUser", "").let {
                val currentUser: User = Gson().fromJson(it, User::class.java)
                binding.tvName.text = getString(R.string.name).format(currentUser.name)
                binding.tvUsername.text = getString(R.string.username).format(currentUser.username)
                binding.tvEmail.text = getString(R.string.email).format(currentUser.email)
                binding.tvBio.text = getString(R.string.bio).format(currentUser.bio)
                binding.tvInterest.text = getString(R.string.interest).format(currentUser.interest)
                binding.tvCompetency.text = getString(R.string.competency).format(currentUser.competency)
                binding.tvBadge.text = getString(R.string.badge).format(currentUser.badge)
                binding.tvCredits.text = getString(R.string.credits).format(currentUser.credits)
                util.glide(context, Uri.parse(currentUser.profilePic), binding.ivProfile)
            }

            binding.tvLogout.setOnClickListener {
                try {
                    profileViewModel.logout()
                    sharedPreferences?.edit()?.clear()?.putBoolean("isLoggedIn", false)?.commit()
                    binding.isLoggedin = false
                    isLoggedIn = false
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Now is not a good time. May be logout later?", Toast.LENGTH_LONG).show()
                }
                Log.d("EX", sharedPreferences?.getString("currentUser", "").toString())
                Log.d("EX", sharedPreferences?.getString("token", "").toString())
                Log.d("EX", sharedPreferences?.getBoolean("isLoggedIn", false).toString())
            }
        }
    }

    fun initObservers() {
        profileViewModel.authTokenMutableLiveData?.observe(viewLifecycleOwner, { token ->
            binding.isLoggedin = token != null
            token.takeIf { it != null }.let {
                sharedPreferences?.edit()?.putString("token", it.toString())?.commit()
            }
        })

        profileViewModel.currentUserMutableLiveData?.observe(viewLifecycleOwner, { user ->
            if(user == null){
                binding.currentUser = null
                return@observe
            }
            Toast.makeText(context, "Signed up successfully! You can login " + user.username, Toast.LENGTH_LONG).show()
            sharedPreferences?.edit()?.putString("username", user.username)?.commit()
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
}