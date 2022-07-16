package com.maandraj.vk_app.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.maandraj.vk_app.di.AppScope
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
@Module
class CoreModule{

    @Provides
    @AppScope
    fun provideSharedPreference(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    @Provides
    @AppScope
    fun provideMoshi(): Moshi =
        Moshi.Builder().build()
}
private const val PREF_NAME = "VKPRef"
