package org.example.project

import android.app.Application
import org.example.project.core.initKoin
import org.koin.android.ext.koin.androidContext


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppHolder.app = this
        initMapKit()
        initKoin(
            config = {androidContext(this@MyApp)}
        )
    }
}

object AppHolder{
    lateinit var app: Application
}