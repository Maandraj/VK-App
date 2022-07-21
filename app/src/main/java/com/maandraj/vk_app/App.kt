package com.maandraj.vk_app

import android.app.Application
import com.maandraj.album_impl.di.AlbumScreenDepsStore
import com.maandraj.auth_impl.di.AuthScreenDepsStore
import com.maandraj.vk_app.di.app.AppComponent
import com.maandraj.vk_app.di.app.DaggerAppComponent
import com.maandraj.vk_app.di.main.MainActivityDepsStore

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        MainActivityDepsStore.deps = appComponent
        AuthScreenDepsStore.deps = appComponent
        AlbumScreenDepsStore.deps = appComponent
    }
}