package com.example.OfferApp

import android.app.Application
import com.cloudinary.android.MediaManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = mapOf(
            "cloud_name" to "dyloasili",
            "api_key" to "272119243465887",
            "api_secret" to "V4ZMOhOrpI1augaCADHeOKrCXTQ"
        )
        MediaManager.init(this, config)
    }
}