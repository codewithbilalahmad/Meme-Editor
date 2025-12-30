package com.muhammad.memes

import android.app.Application
import com.muhammad.memes.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MemeApplication : Application(){
    companion object{
        lateinit var INSTANCE : MemeApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        startKoin {
            androidContext(this@MemeApplication)
            androidLogger()
            modules(appModule)
        }
    }
}