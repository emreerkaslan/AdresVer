package com.erkaslan.servio.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface UserService {

    @GET("user/{id}")
    fun getUsers(@Path("id") id: Int): Call<User>

    @GET("service/{id}")
    fun getService(@Path("id") id: Int): Call<Service>

    @GET("service/")
    fun getAllServices(): Call<List<Service>>

}