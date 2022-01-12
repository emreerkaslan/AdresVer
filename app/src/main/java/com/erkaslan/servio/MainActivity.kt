package com.erkaslan.servio

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.erkaslan.servio.databinding.ActivityMainBinding
import com.erkaslan.servio.manager.RunOnceManager
import com.erkaslan.servio.model.*
import com.google.gson.Gson
import com.shivtechs.maplocationpicker.MapUtility
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val PREFS_NAME = "Preferences"
    val PREF_USERNAME = "Username"
    val PREF_PASSWORD = "Password"
    val PREF_TOKEN = "Token"
    val PREF_PK = "Pk"
    private lateinit var binding: ActivityMainBinding
    var currentUser: User? = null
    var isLoggedIn: Boolean = false
    var token: Token? = null
    val service = RetrofitClient.getClient().create(UserService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_add, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        //RunOnceManager().runOnce(this, )
        authenticate(service)
        MapUtility.apiKey = getResources().getString(R.string.google_maps_key);
    }


    fun authenticate(userService: UserService){
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = prefs.edit()

        //Check authentication
        val token: String? = prefs.getString("Token", "null")
        val username: String? = prefs.getString("Username", "null")
        if(token.equals("null") || token.isNullOrEmpty() || username.equals("null") || username.isNullOrEmpty()){
            editor.putBoolean("isLoggedIn", false)?.commit()
            isLoggedIn = false
        }else{
            Log.d("EX", "Token: " + token + "Username: " + username)
            val check = userService.loginCheck("Token " + token, username)
            check.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "User is logged in as user: " + response.body()?.pk.toString() , Toast.LENGTH_LONG).show()
                        val user = response.body() as User
                        val gson = Gson()
                        val json = gson.toJson(user)
                        editor.putBoolean("isLoggedIn", true)?.putString("currentUser", json)?.commit()
                        isLoggedIn = true
                        currentUser = user
                        Log.d("EX", "User is logged in as user: " + currentUser)
                    } else if (response.code() == 400) {
                        editor.putBoolean("isLoggedIn", false)?.commit()
                        isLoggedIn = false
                        Log.d("EX",response.errorBody().toString())
                    }
                    else if (response.code() == 301){
                        editor.putBoolean("isLoggedIn", false)?.commit()
                        isLoggedIn = false
                        Log.d("EX", "Not Successfull")
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    editor.putBoolean("isLoggedIn", false)?.commit()
                    isLoggedIn = false
                    Log.d("EX", "Failed: " + t.toString())
                }
            })
        }
    }
}

