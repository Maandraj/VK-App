package com.maandraj.vk_app.ui.main.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.maandraj.album_api.AlbumFeatureApi
import com.maandraj.album_impl.ui.navigation.AlbumFeatureImpl
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.auth_impl.ui.navigation.AuthFeatureImpl
import com.maandraj.core.data.config.ConfigRepoImpl
import com.maandraj.core.data.result.ResultOf
import com.maandraj.core.utils.constants.KEY_SNACKBAR
import com.maandraj.vk_app.App
import com.maandraj.vk_app.di.main.DaggerMainComponent
import com.maandraj.vk_app.di.main.MainActivityDepsProvider
import com.maandraj.vk_app.domain.interator.MainInteractor
import com.maandraj.vk_app.ui.main.content.AppContent
import com.maandraj.vk_app.ui.theme.VKAppTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
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

    @Inject
    lateinit var albumFeatureApi: AlbumFeatureApi

    private val tokenTracker = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
            viewModel.logout()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authFeatureApi = appComponent.authFeatureApi
        albumFeatureApi = appComponent.albumFeatureApi
        VK.addTokenExpiredHandler(tokenTracker)
        setContent {
            VKAppTheme {
                val scaffoldState = rememberScaffoldState()
                val isNeedLogout: ResultOf<Boolean> by viewModel.isLogout.observeAsState(ResultOf.Success(
                    false))
                isNeedLogout.let {
                    when (it) {
                        is ResultOf.Success -> {
                            if (it.value) {
                                LaunchedEffect(KEY_SNACKBAR) {
                                    scaffoldState.snackbarHostState.showSnackbar(getString(com.maandraj.core.R.string.error_account_time_expired))
                                }
                                AppContent(
                                    isNeedLogout = it.value,
                                    authFeatureApi = authFeatureApi,
                                    albumFeatureApi = albumFeatureApi,
                                    viewModel = viewModel
                                )
                            } else {
                                AppContent(
                                    isNeedLogout = false,
                                    authFeatureApi = authFeatureApi,
                                    albumFeatureApi = albumFeatureApi,
                                    viewModel = viewModel
                                )
                            }
                        }
                        is ResultOf.Failure -> {
                            LaunchedEffect(KEY_SNACKBAR) {
                                scaffoldState.snackbarHostState.showSnackbar(it.message)
                            }
                            AppContent(
                                isNeedLogout = true,
                                authFeatureApi = authFeatureApi,
                                albumFeatureApi = albumFeatureApi,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        VK.removeTokenExpiredHandler(tokenTracker)
        super.onDestroy()
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
        AppContent(isNeedLogout = false,
            authFeatureApi = AuthFeatureImpl(),
            albumFeatureApi = AlbumFeatureImpl(),
            viewModel = MainActivityVM(mainInteractor)
        )
    }
}

