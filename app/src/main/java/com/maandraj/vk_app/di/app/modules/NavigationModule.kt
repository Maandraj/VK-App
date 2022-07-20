package com.maandraj.vk_app.di.app.modules

import com.maandraj.album_api.AlbumFeatureApi
import com.maandraj.album_impl.ui.navigation.AlbumFeatureImpl
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.auth_impl.ui.navigation.AuthFeatureImpl
import dagger.Module
import dagger.Provides

@Module
class NavigationModule {
    @Provides
    fun provideAuthFeatureApi(): AuthFeatureApi = AuthFeatureImpl()

    @Provides
    fun provideAlbumFeatureApi(): AlbumFeatureApi = AlbumFeatureImpl()
}