package com.maandraj.vk_app.di.app.modules

import com.maandraj.album_impl.di.AlbumModule
import dagger.Module


@Module(includes = [
    AlbumModule::class,
    DataModule::class,
    NavigationModule::class
])
class AppModule




