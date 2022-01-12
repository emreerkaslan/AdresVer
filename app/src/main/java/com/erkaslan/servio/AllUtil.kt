package com.erkaslan.servio

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.JsonObject
import java.util.*
import kotlin.collections.HashMap


class AllUtil () {

    fun glide(context: Context, uri: Uri?, imageView: ImageView?) {
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .override(500)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .placeholder(R.drawable.ic_default_profile_pic)
            .into(imageView!!)
    }

    fun glideCircle(context: Context, uri: Uri?, imageView: ImageView?) {
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .circleCrop()
            .override(500)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .placeholder(R.drawable.ic_default_profile_pic)
            .into(imageView!!)
    }

    fun validateSignup(
        username: String,
        name: String,
        password: String,
        passwordAgain: String,
        email: String,
        geolocation: String,
        bio: String,
        interest: String,
        competency: String
    ): HashMap<Boolean, String> {
        val map = HashMap<Boolean, String>()
        if (password.length < 8) {
            map.put(false, "Password length must be at least 8 characters")
            return map
        }
        if (!password.equals(passwordAgain)) {
            map.put(false, "Passwords are not matching")
            return map
        }
        if (name.isNullOrEmpty()) {
            map.put(false, "Enter a name")
            return map
        }
        if (username.isNullOrEmpty()) {
            map.put(false, "Enter a unique username")
            return map
        }
        if (email.isNullOrEmpty()) {
            map.put(false, "Enter your email")
            return map
        }
        if (geolocation.isNullOrEmpty()) {
            map.put(false, "Enter your location")
            return map
        }
        if (interest.isNullOrEmpty()) {
            map.put(false, "You must enter your interest")
            return map
        }
        if (competency.isNullOrEmpty()) {
            map.put(false, "You must have a skill to serve community")
            return map
        }
        if (bio.isNullOrEmpty()) {
            map.put(false, "Enter your bio please")
            return map
        }
        if (!email.trim().contains("@") || !email.trim().contains(".") || email.replace("@", "")
                .replace(".", "").length <= 0
        ) {
            map.put(false, "Check your email please")
            return map
        }

        map.put(true, "Alright")
        return map
    }

    fun validateServiceCreation(service: JsonObject): JsonObject {
        /*
        if (service.get("title").isJsonNull || service.get("title").asString.length < 5) {
            service.addProperty("message", "Title is missing or short")
            service.addProperty("result", false)
            return service
        }

        if (service.get("description").isJsonNull || service.get("description").asString.length < 5) {
            service.addProperty("message", "Description is missing or short")
            service.addProperty("result", false)
            return service
        }

        if (service.get("credits").isNullOrEmpty()) {
            map.put("message", "Credit amount is missing")
            map.put("result", "false")
            return map
        }

        if (Integer.parseInt(service.get("credits")) < 0 || Integer.parseInt(service.get("credits")) > 15) {
            map.put("message", "Credit amount can't be less than 0 or more than 15")
            map.put("result", "false")
            return map
        }

        if (service.get("geolocation").isNullOrEmpty()) {
            map.put("message", "Geolocation is missing")
            map.put("result", "false")
            return map
        }

        if (service.get("recurring").isNullOrEmpty()) {
            map.put("message", "Recurring info is missing")
            map.put("result", "false")
            return map
        }

        if (service.get("date").isNullOrEmpty()) {
            map.put("message", "Date is missing")
            map.put("result", "false")
            return map
        }
        service.put("result", "true")
        */
        return service
    }
}