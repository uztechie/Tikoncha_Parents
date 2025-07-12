package org.example.project

import android.app.Application
import org.example.project.core.initKoin


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initMapKit()
        initKoin {}
    }
}