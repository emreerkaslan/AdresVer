package com.erkaslan.servio.model

import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @GET("user/{id}")
    fun getUsers(@Path("id") id: Int): Call<User>

    @GET("service/{id}")
    fun getService(@Path("id") id: Int): Call<Service>

    @GET("service/")
    fun getAllServices(): Call<List<Service>>

    @POST("user/login/")
    fun userLogin(@Body credentials: HashMap<String, String>): Call<Token>

    @GET("user/{username}/")
    fun loginCheck(@Header("Authorization") token: String, @Path("username") username: String): Call<User>
}