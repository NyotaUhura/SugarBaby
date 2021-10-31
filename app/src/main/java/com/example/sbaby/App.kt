package com.example.sbaby

import android.app.Application
import com.airbnb.mvrx.Mavericks
import com.example.sbaby.di.module
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Mavericks.initialize(this)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(module)
        }
    }
}
