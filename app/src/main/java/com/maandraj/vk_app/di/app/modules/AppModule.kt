package com.maandraj.vk_app.di.app.modules

import com.maandraj.auth_impl.di.AuthModule
import dagger.Module


@Module(includes = [
    NetworkModule::class,
    CoreModule::class,
    AuthModule::class,
    DataModule::class,
    NavigationModule::class
])
class AppModule




