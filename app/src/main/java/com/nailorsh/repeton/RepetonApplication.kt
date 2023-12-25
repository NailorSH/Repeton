package com.nailorsh.repeton

import android.app.Application
import com.nailorsh.repeton.data.AppContainer
import com.nailorsh.repeton.data.DefaultAppContainer

class RepetonApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}