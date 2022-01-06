package com.erkaslan.servio.model

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.erkaslan.servio.model.RetrofitClient.getClient
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Error
import java.util.*
import kotlin.collections.HashMap

class Repository(private val application: Application) : ServiceInterface, AllServicesInterface, LoginInterface {
    lateinit var userService: UserService
    lateinit var userSingle: MutableLiveData<User>

    override fun onSuccess(service: Service) {}
    override fun onAllServicesTaken(listOfServices: List<Service>) {}
    override fun onLogin(token: Token) {}
    override fun onFailure(servioException: ServioException) {}

    //Gets a all services
    fun getAllServices(serviceInterface: AllServicesInterface){
        userService = getClient().create(UserService::class.java)
        var service = userService.getAllServices()

        service.enqueue(object : Callback<List<Service>> {
            override fun onResponse(call: Call<List<Service>>, response: Response<List<Service>>) {
                Log.d("REPO", response.body().toString())
                if (response.isSuccessful) {
                    serviceInterface.onAllServicesTaken(response.body() as List<Service>)
                } else if (response.code() == 400) {
                    Log.v("VER",response.errorBody().toString());
                }
                else{
                }
            }

            override fun onFailure(call: Call<List<Service>>, t: Throwable) {
            }
        })
    }

    //Gets a all events
    fun getAllEvents(serviceInterface: AllEventsInterface){
        userService = getClient().create(UserService::class.java)
        var event = userService.getAllEvents()

        event.enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                Log.d("REPO", response.body().toString())
                if (response.isSuccessful) {
                    serviceInterface.onAllEventsTaken(response.body() as List<Event>)
                } else if (response.code() == 400) {
                    Log.v("VER",response.errorBody().toString());
                }
                else{
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
            }
        })
    }

    //Gets a single service with id
    fun getService(id: Int , serviceInterface: ServiceInterface){
        userService = getClient().create(UserService::class.java)
        var service = userService.getService(id)

        service.enqueue(object : Callback<Service> {
            override fun onResponse(call: Call<Service>, response: Response<Service>) {
                Log.d("EX", response.body().toString())
                if (response.isSuccessful) {
                    serviceInterface.onSuccess(response.body() as Service)
                } else if (response.code() == 400) {
                    Log.v("VER",response.errorBody().toString());
                }
                else{
                }
            }

            override fun onFailure(call: Call<Service>, t: Throwable) {
            }
        })
    }

    //Logs user in
    fun login(map: HashMap<String, String>, loginInterface: LoginInterface){
        var service = getClient().create(UserService::class.java).userLogin(map)
        service.enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if (response.isSuccessful) {
                    loginInterface.onLogin(response.body() as Token)
                } else {
                    loginInterface.onFailure(ServioException("Failed Login"))
                }
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                loginInterface.onFailure(ServioException("Failed Login"))
            }
        })
    }


    //Gets user info
    fun loginCheck(token: String, username: String, loginCheckInterface: LoginCheckInterface){
        var service = getClient().create(UserService::class.java).loginCheck("Token " + token, username)
        service.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    loginCheckInterface.onLoginCheck(response.body() as User)
                } else if (response.code() == 400) {
                    loginCheckInterface.onLoginFailure(ServioException("Failed Login"))
                }
                else if (response.code() == 301){
                    loginCheckInterface.onLoginFailure(ServioException("Failed Login"))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                loginCheckInterface.onLoginFailure(ServioException("Failed Login"))
            }
        })
    }

    //Signs user up
    fun signup(map: JSONObject, signupInterface: SignupInterface){
        var service = getClient().create(UserService::class.java).signup(map)
        Log.d("EX", "YYY: " + map)
        service.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    signupInterface.onSignup(response.body() as User)
                } else {
                    signupInterface.onSignupFailure(ServioException("Failed Signup"))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                signupInterface.onSignupFailure(ServioException("Failed Signup"))
            }
        })
    }

    //Creates service
    fun createService(service: Service, createServiceInterface: CreateServiceInterface){
        var service = getClient().create(UserService::class.java).createService(service)
        service.enqueue(object : Callback<Service> {
            override fun onResponse(call: Call<Service>, response: Response<Service>) {
                if (response.isSuccessful) {
                    createServiceInterface.onCreateService(response.body() as Service)
                } else {
                    createServiceInterface.onCreateServiceFailure(ServioException("Failed Service Creation"))
                }
            }

            override fun onFailure(call: Call<Service>, t: Throwable) {
                createServiceInterface.onCreateServiceFailure(ServioException("Failed Service Creation"))
            }
        })
    }

    //Creates event
    fun createEvent(map: HashMap<String, String>, createEventInterface: CreateEventInterface){
        var event = getClient().create(UserService::class.java).createEvent(map)
        event.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    createEventInterface.onCreateEvent(response.body() as Event)
                } else {
                    createEventInterface.onCreateEventFailure(ServioException("Failed Service Creation"))
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                createEventInterface.onCreateEventFailure(ServioException("Failed Service Creation"))
            }
        })
    }
}
