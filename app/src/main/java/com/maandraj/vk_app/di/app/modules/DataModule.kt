package com.maandraj.vk_app.di.app.modules

import android.content.Context
import android.content.SharedPreferences
import com.maandraj.core.data.config.ConfigRepo
import com.maandraj.core.data.config.ConfigRepoImpl
import com.maandraj.vk_app.di.app.AppScope
import dagger.Module
import dagger.Provides

@Module
class DataModule {
    @Provides
    @AppScope
    fun provideConfigRepo(sharedPref: SharedPreferences, context: Context): ConfigRepo =
        ConfigRepoImpl(
            sharedPref = sharedPref,
            context = context
        )
}