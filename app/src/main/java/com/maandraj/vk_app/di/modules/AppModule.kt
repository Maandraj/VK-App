package com.maandraj.vk_app.di.modules

import dagger.Module


@Module(includes = [
    NetworkModule::class,
    CoreModule::class,
    DataModule::class,
    NavigationModule::class
])
class AppModule




