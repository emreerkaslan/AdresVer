package com.erkaslan.servio.model

import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.HashMap

interface UserService {

    @GET("user/{id}")
    fun getUser(@Path("id") id: Int): Call<User>

    @POST("user/set/")
    fun getUsers(@Header("Authorization") token: String, @Body data: JSONObject): Call<List<User>>

    @POST("user/login/")
    fun userLogin(@Body credentials: HashMap<String, String>): Call<Token>

    @GET("user/{username}/")
    fun loginCheck(@Header("Authorization") token: String, @Path("username") username: String): Call<User>
    
    @POST("user/create/")
    fun signup(@Body data: JSONObject): Call<User>

    @GET("service/{id}/set/")
    fun getServices(@Header("Authorization") token: String, @Path("id") id: Int): Call<List<Service>>

    @GET("service/{id}")
    fun getService(@Path("id") id: Int): Call<Service>

    @GET("service/")
    fun getAllServices(): Call<List<Service>>

    @POST("service/create/")
    fun createService(@Header("Authorization") token: String, @Body service: JsonObject): Call<Service>

    @POST("service/accept/{service}/{user}/")
    fun acceptRequest(@Header("Authorization") token: String, @Path("service") service: Int, @Path("user") user: Int): Call<Service>

    @POST("service/decline/{service}/{user}/")
    fun declineRequest(@Header("Authorization") token: String, @Path("service") service: Int, @Path("user") user: Int): Call<Service>

    @POST("service/request/{service}/{user}/")
    fun addRequest(@Header("Authorization") token: String, @Path("service") service: Int, @Path("user") user: Int): Call<Service>

    @POST("service/feedback/{service}/{feedback}/")
    fun addServiceFeedback(@Header("Authorization") token: String, @Path("service") service: Int, @Path("feedback") feedback: Int): Call<Service>

    @GET("event/{id}")
    fun getEvent(@Path("id") id: Int): Call<Event>

    @GET("event/")
    fun getAllEvents(): Call<List<Event>>

    @POST("event/create/")
    fun createEvent(@Header("Authorization") token: String, @Body event: JsonObject): Call<Event>

    @POST("event/attend/{event}/{user}")
    fun attend(@Header("Authorization") token: String, @Path("event") event: Int, @Path("user") user: Int): Call<Event>

    @POST("feedback/create/")
    fun addFeedback(@Header("Authorization") token: String, @Body event: JsonObject): Call<Feedback>

    @POST("feedback/set/")
    fun getFeedbacks(@Body data: JSONObject): Call<List<Feedback>>

    @POST("user/addcredits/{user}/{credits}/")
    fun addCredits(@Header("Authorization") token: String, @Path("user") user: Int, @Path("credits") credits: Int): Call<User>
}