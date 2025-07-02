package org.example.project

import android.app.Application
import org.example.project.core.initKoin
import org.koin.android.ext.koin.androidContext

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin{
            androidContext(this@MyApp)
        }
    }
}