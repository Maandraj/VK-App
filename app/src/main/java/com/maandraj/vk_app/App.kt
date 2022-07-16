package com.maandraj.vk_app

import android.app.Application
import android.content.SharedPreferences
import com.maandraj.auth_impl.di.AuthScreenDeps
import com.maandraj.auth_impl.di.AuthScreenDepsStore
import com.maandraj.vk_app.di.AppComponent
import com.maandraj.vk_app.di.DaggerAppComponent

class App :Application(){
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }
    override fun onCreate() {
        super.onCreate()
        AuthScreenDepsStore.deps = appComponent
    }
}