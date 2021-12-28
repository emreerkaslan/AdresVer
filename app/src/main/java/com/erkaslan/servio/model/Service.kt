package com.erkaslan.servio.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class Service(
    @SerializedName("pk")
    @Expose
    var pk: Int = 0,
    @SerializedName("title")
    @Expose
    var title: String = "",
    @SerializedName("description")
    @Expose
    var description: String = "",
    @SerializedName("date")
    @Expose
    var date: Date,
    @SerializedName("geolocation")
    @Expose
    var geolocation: String = "",
    @SerializedName("giver")
    @Expose
    var giver: Int,
    @SerializedName("credits")
    @Expose
    var credits: Int
)