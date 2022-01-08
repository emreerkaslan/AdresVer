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
    var date: Date? = null,
    @SerializedName("geolocation")
    @Expose
    var geolocation: String = "",
    @SerializedName("picture")
    @Expose
    var picture: String = "",
    @SerializedName("giver")
    @Expose
    var giver: Int,
    @SerializedName("taker")
    @Expose
    var taker: List<Int>? = null,
    @SerializedName("feedbackList")
    @Expose
    var feedbackList: List<Int>?,
    @SerializedName("recurring")
    @Expose
    var recurring: Boolean?  = null,
    @SerializedName("credits")
    @Expose
    var credits: Int,
    @SerializedName("requests")
    @Expose
    var requests: List<Int>? = null,
    @SerializedName("tags")
    @Expose
    var tags: List<String>? = null
)