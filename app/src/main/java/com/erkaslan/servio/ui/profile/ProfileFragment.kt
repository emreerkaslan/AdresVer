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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.SharedPreferences

import android.content.Context.MODE_PRIVATE
import android.content.Context.MODE_WORLD_WRITEABLE


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        initViews()
        initObservers()
    }

    fun initViews(){
        binding.btnLogin.setOnClickListener {
            login(binding.etUsername.text.toString(), binding.etPassword.text.toString())
        }
    }

    fun initObservers(){

    }

    fun login(username: String, password: String){
        val map = HashMap<String, String>()
        map.put("username", username)
        map.put("password", password)
        var service = RetrofitClient.getClient().create(UserService::class.java).userLogin(map)
        service.enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                Log.d("EX", response.body().toString())
                if (response.isSuccessful) {
                    //serviceInterface.onSuccess(response.body() as Service)
                    Toast.makeText(context, response.body().toString(), Toast.LENGTH_LONG).show()
                    val token = response.body()?.token.toString()
                    var sharedPreferences: SharedPreferences.Editor? = context?.getSharedPreferences("app", MODE_PRIVATE)?.edit()
                    if (sharedPreferences != null) {
                        sharedPreferences.putString("token", token )
                        sharedPreferences.putString("username", username)
                        sharedPreferences.commit()
                    }
                } else if (response.code() == 400) {
                    Log.v("VER",response.errorBody().toString());
                }
                else{
                }
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
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