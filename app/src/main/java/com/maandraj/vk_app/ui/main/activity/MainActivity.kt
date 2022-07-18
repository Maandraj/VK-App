package com.maandraj.vk_app.ui.main.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.auth_impl.presentation.navigation.AuthFeatureImpl
import com.maandraj.core.data.config.ConfigRepoImpl
import com.maandraj.vk_app.App
import com.maandraj.vk_app.di.main.DaggerMainComponent
import com.maandraj.vk_app.di.main.MainActivityDepsProvider
import com.maandraj.vk_app.domain.interators.MainInteractor
import com.maandraj.vk_app.ui.main.content.AppContent
import com.maandraj.vk_app.ui.theme.VKAppTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    private val appComponent by lazy {
        (application as App).appComponent
    }
    private val viewModel = DaggerMainComponent.builder()
        .mainActivityDeps(MainActivityDepsProvider.deps)
        .build().getViewModel()

    @Inject
    lateinit var authFeatureApi: AuthFeatureApi


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authFeatureApi = appComponent.authFeatureApi

        setContent {
            VKAppTheme {
                AppContent(
                    authFeatureApi = authFeatureApi,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current
    val shared = context.getSharedPreferences("", Context.MODE_PRIVATE)
    val configRepo = ConfigRepoImpl(shared, context)
    val mainInteractor = MainInteractor(configRepo)
    VKAppTheme {
        AppContent(authFeatureApi = AuthFeatureImpl(),
            viewModel = MainActivityVM(mainInteractor)
        )
    }
}