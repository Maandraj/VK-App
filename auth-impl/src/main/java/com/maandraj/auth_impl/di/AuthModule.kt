package com.maandraj.auth_impl.di

import com.maandraj.auth_impl.domain.AuthInteractor
import com.maandraj.auth_impl.presentation.authScreen.AuthVM
import dagger.Module
import dagger.Provides
@Module
class AuthModule {
    @Provides
    @AuthScreenScope
    fun provideViewModel(authInteractor: AuthInteractor): AuthVM = AuthVM(authInteractor = authInteractor )
}