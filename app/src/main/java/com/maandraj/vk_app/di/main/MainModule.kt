package com.maandraj.vk_app.di.main

import com.maandraj.vk_app.domain.interaсtor.MainInteractor
import com.maandraj.vk_app.ui.main.activity.MainActivityVM
import dagger.Module
import dagger.Provides

@Module
class MainModule {
    @Provides
    @MainScope
    fun provideViewModel(mainInteractor: MainInteractor): MainActivityVM =
        MainActivityVM(mainInteractor = mainInteractor)
}