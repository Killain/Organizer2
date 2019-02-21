package com.killain.organizer.packages.app

import android.app.Application

import com.squareup.leakcanary.LeakCanary

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
    }
}
