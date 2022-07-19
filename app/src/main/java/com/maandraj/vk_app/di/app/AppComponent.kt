package com.maandraj.vk_app.di.app

import android.content.Context
import com.maandraj.album_impl.di.AlbumScreenDeps
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.auth_impl.di.AuthScreenDeps
import com.maandraj.core.data.config.ConfigRepo
import com.maandraj.vk_app.di.app.modules.AppModule
import com.maandraj.vk_app.di.main.MainActivityDeps
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent : AuthScreenDeps, MainActivityDeps, AlbumScreenDeps {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(context: Context): Builder
        fun build(): AppComponent
    }

    override val applicationContext: Context
    override val configRepo: ConfigRepo
    override val authFeatureApi: AuthFeatureApi
}

@Scope
annotation class AppScope