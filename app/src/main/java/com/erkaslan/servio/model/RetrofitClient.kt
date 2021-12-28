package com.erkaslan.servio.model

import android.accessibilityservice.GestureDescription
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient{
    var BASE_URL = "http://10.0.2.2:8000/"
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        //val interceptor = HttpLoggingInterceptor()
        if (retrofit == null)
        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        //val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
            retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit as Retrofit
    }
}