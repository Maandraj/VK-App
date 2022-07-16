package com.maandraj.auth_impl.di

import androidx.annotation.RestrictTo
import com.maandraj.auth_impl.presentation.authScreen.AuthVM
import dagger.Component
import javax.inject.Scope
import kotlin.properties.Delegates

@Component(
    modules = [],
    dependencies = [AuthScreenDeps::class])
internal interface AuthComponent {
    @Component.Builder
    interface Builder {

        fun authScreenDeps(welcomeScreenDeps: AuthScreenDeps): Builder

        fun build(): AuthComponent
    }
    fun getViewModel() : AuthVM
}

interface AuthScreenDeps {

}
interface AuthScreenDepsProvider {
    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val deps: AuthScreenDeps
    companion object : AuthScreenDepsProvider by AuthScreenDepsStore
}

object AuthScreenDepsStore : AuthScreenDepsProvider {
    override var deps: AuthScreenDeps by Delegates.notNull()
}
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class AuthScreenScope