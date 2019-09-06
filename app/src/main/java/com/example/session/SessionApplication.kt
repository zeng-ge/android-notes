package com.example.session

import android.app.Application
import com.facebook.stetho.Stetho

class SessionApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            //开启chrome调试
            Stetho.initializeWithDefaults(this)
        }

    }
}