package com.maandraj.vk_app.di.app.modules

import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.auth_impl.presentation.navigation.AuthFeatureImpl
import dagger.Module
import dagger.Provides

@Module
class NavigationModule {
    @Provides
    fun provideAuthFeatureApi(): AuthFeatureApi = AuthFeatureImpl()
}