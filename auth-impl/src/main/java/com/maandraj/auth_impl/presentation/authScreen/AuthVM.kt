package com.maandraj.auth_impl.presentation.authScreen

import androidx.lifecycle.ViewModel
import com.maandraj.auth_impl.domain.AuthInteractor
import javax.inject.Inject

class AuthVM(
    private val authInteractor: AuthInteractor
): ViewModel() {
}