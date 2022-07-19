package com.maandraj.vk_app.di.app.modules

import com.maandraj.album_impl.di.AlbumModule
import dagger.Module


@Module(includes = [
    NetworkModule::class,
    CoreModule::class,
    AlbumModule::class,
    DataModule::class,
    NavigationModule::class
])
class AppModule {
}




