package com.maandraj.vk_app.di.modules

import android.content.SharedPreferences
import com.maandraj.auth_impl.data.AuthRepo
import com.maandraj.auth_impl.data.AuthRepoImpl
import com.maandraj.core.data.config.ConfigRepo
import com.maandraj.core.data.config.ConfigRepoImpl
import com.maandraj.vk_app.di.AppScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule{
    @Provides
    @AppScope
    fun provideConfigRepo(sharedPref: SharedPreferences): ConfigRepo =
        ConfigRepoImpl(
            sharedPref = sharedPref
        )
    @Provides
    @AppScope
    fun provideAuthRepo(): AuthRepo =
       AuthRepoImpl()
}