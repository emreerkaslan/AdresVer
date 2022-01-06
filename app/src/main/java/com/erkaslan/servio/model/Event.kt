package com.erkaslan.servio.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class Event(
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
    @SerializedName("picture")
    @Expose
    var picture: String,
    @SerializedName("organizer")
    @Expose
    var organizer: Int,
    @SerializedName("address")
    @Expose
    var address: String,
    @SerializedName("hasQuota")
    @Expose
    var hasQuota: Boolean,
    @SerializedName("quota")
    @Expose
    var quota: Int,
    @SerializedName("attendees")
    @Expose
    var attendees: List<User>
)