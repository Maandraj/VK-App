package com.maandraj.auth_impl.di

import android.content.Context
import androidx.annotation.RestrictTo
import com.maandraj.auth_impl.presentation.authScreen.AuthVM
import com.maandraj.core.data.config.ConfigRepo
import dagger.Component
import javax.inject.Scope
import kotlin.properties.Delegates

@AuthScreenScope
@Component(
    modules = [AuthModule::class],
    dependencies = [AuthScreenDeps::class])
internal interface AuthComponent {
    @Component.Builder
    interface Builder {
        fun authScreenDeps(authScreenDeps: AuthScreenDeps): Builder

        fun build(): AuthComponent
    }

    fun getViewModel(): AuthVM
}

interface AuthScreenDeps {
    val applicationContext: Context
    val configRepo: ConfigRepo
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
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthScreenScope