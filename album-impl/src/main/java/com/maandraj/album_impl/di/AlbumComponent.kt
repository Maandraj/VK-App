package com.maandraj.album_impl.di

import android.content.Context
import androidx.annotation.RestrictTo
import com.maandraj.album_impl.ui.album.AlbumScreenVM
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.core.data.config.ConfigRepo
import dagger.Component
import javax.inject.Scope
import kotlin.properties.Delegates

@AlbumScreenScope
@Component(modules = [AlbumModule::class],
    dependencies = [AlbumScreenDeps::class])
internal interface AlbumComponent {
    @Component.Builder
    interface Builder {
        fun albumDeps(albumScreenDeps: AlbumScreenDeps): Builder

        fun build(): AlbumComponent
    }

    fun getViewModel(): AlbumScreenVM
}

interface AlbumScreenDeps {
    val authFeatureApi: AuthFeatureApi
    val applicationContext: Context
    val configRepo: ConfigRepo
}

interface AlbumScreenDepsProvider {
    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val deps: AlbumScreenDeps

    companion object : AlbumScreenDepsProvider by AlbumScreenDepsStore
}

object AlbumScreenDepsStore : AlbumScreenDepsProvider {
    override var deps: AlbumScreenDeps by Delegates.notNull()
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AlbumScreenScope