package com.erkaslan.servio.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Feedback(
    @SerializedName("pk")
    @Expose
    var pk: Int = 0,
    @SerializedName("rating")
    @Expose
    var rating: Int = 5,
    @SerializedName("comment")
    @Expose
    var comment: String = "",
    @SerializedName("service")
    @Expose
    var service: Service,
    @SerializedName("giver")
    @Expose
    var giver: User,
    @SerializedName("taker")
    @Expose
    var taker: User,
)
