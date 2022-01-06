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
import com.erkaslan.servio.MainActivity
import com.erkaslan.servio.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import java.lang.Exception


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    val prefs = activity?.getSharedPreferences((activity as MainActivity).PREFS_NAME, MODE_PRIVATE)
    val editor = prefs?.edit()
    private var util = AllUtil()
    private var username: String = "default"


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
                val gson = Gson()
                val json = JSONObject(gson.toJson(map))
                profileViewModel.signup(json)
/*
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
                */
            }
        } else {
            binding.currentUser = (activity as MainActivity).currentUser

            //Sets logout button click
            binding.tvLogout.setOnClickListener {
                try {
                    profileViewModel.logout()
                    editor?.clear()?.putBoolean("isLoggedIn", false)?.commit()
                    binding.isLoggedin = false
                    isLoggedIn = false
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Now is not a good time. May be logout later?", Toast.LENGTH_LONG).show()
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
                        Toast.makeText(context, "Token: " + token, Toast.LENGTH_LONG).show()
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
                    Toast.makeText(context, "Signed up successfully! You can login " + it.data.username, Toast.LENGTH_LONG).show()
                    prefs?.edit()?.putString("username", it.data.username)?.commit()
                    binding.currentUser = it.data
                    (activity as MainActivity).currentUser = it.data
                    Log.d("EX", "currentUser: " + it.data.toString())
                }
                is GenericResult.Failure -> {
                    Toast.makeText(context, "Something is wrong, try singup again", Toast.LENGTH_LONG).show()
                }
                else -> {}
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
}