package com.erkaslan.servio.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("pk")
    @Expose
    var pk: Int = 0,
    @SerializedName("username")
    @Expose
    var username: String = "",
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("email")
    @Expose
    var email: String = "",
    @SerializedName("bio")
    @Expose
    var bio: String = "",
    @SerializedName("geolocation")
    @Expose
    var geolocation: String = "",
    @SerializedName("interest")
    @Expose
    var interest: String = "",
    @SerializedName("competency")
    @Expose
    var competency: String = ""
)