package com.maandraj.vk_app.di

import android.app.Application
import android.content.Context
import com.maandraj.auth_impl.di.AuthScreenDeps
import com.maandraj.vk_app.di.modules.AppModule
import com.maandraj.vk_app.ui.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent : AuthScreenDeps{
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(context: Context): Builder

        fun build(): AppComponent
    }

}

@Scope
annotation class AppScope