package com.maandraj.vk_app.di.main

import androidx.annotation.RestrictTo
import com.maandraj.album_api.AlbumFeatureApi
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.core.data.config.ConfigRepo
import com.maandraj.vk_app.ui.main.activity.MainActivity
import com.maandraj.vk_app.ui.main.activity.MainActivityVM
import dagger.Component
import javax.inject.Scope
import kotlin.properties.Delegates

@MainScope
@Component(modules = [MainModule::class],
    dependencies = [MainActivityDeps::class])
internal interface MainComponent {
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        fun mainActivityDeps(mainActivityDeps: MainActivityDeps): Builder

        fun build(): MainComponent
    }
    fun getViewModel(): MainActivityVM
}

interface MainActivityDeps {
    val authFeatureApi: AuthFeatureApi
    val albumFeatureApi: AlbumFeatureApi
    val configRepo: ConfigRepo
}

interface MainActivityDepsProvider {
    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val deps: MainActivityDeps

    companion object : MainActivityDepsProvider by MainActivityDepsStore
}

object MainActivityDepsStore : MainActivityDepsProvider {
    override var deps: MainActivityDeps by Delegates.notNull()
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScope