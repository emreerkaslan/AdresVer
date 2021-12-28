package com.erkaslan.servio

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ServioApplication : Application() {
    var userAuthToken: String = ""
    var isAuthenticated: Boolean = false
}