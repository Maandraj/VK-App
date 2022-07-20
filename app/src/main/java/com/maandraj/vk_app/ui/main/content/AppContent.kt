package com.maandraj.vk_app.ui.main.content

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.maandraj.album_api.AlbumFeatureApi
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.vk_app.ui.main.activity.MainActivityVM
import com.maandraj.vk_app.ui.main.graph.SetGraph
import com.maandraj.vk_app.ui.theme.VKAppTheme
import com.maandraj.vk_app.utils.NavigationGraph


@Composable
fun AppContent(
    isNeedLogout: Boolean,
    authFeatureApi: AuthFeatureApi,
    albumFeatureApi: AlbumFeatureApi,
    viewModel: MainActivityVM,
) {
    ProvideWindowInsets {
        VKAppTheme() {
            val navController = rememberNavController()
            Scaffold() { innerPaddingModifier ->
                if (viewModel.isLogged()) {
                    if (isNeedLogout) {
                        SetGraph(
                            navigationGraph = NavigationGraph.UNSAFE,
                            navController = navController,
                            modifier = Modifier.padding(innerPaddingModifier),
                            albumFeatureApi = albumFeatureApi,
                            authFeatureApi = authFeatureApi,
                        )
                    } else {
                        SetGraph(
                            navigationGraph = NavigationGraph.SAFE,
                            navController = navController,
                            modifier = Modifier.padding(innerPaddingModifier),
                            albumFeatureApi = albumFeatureApi,
                            authFeatureApi = authFeatureApi,
                        )

                    }
                } else {
                    SetGraph(
                        navigationGraph = NavigationGraph.UNSAFE,
                        navController = navController,
                        modifier = Modifier.padding(innerPaddingModifier),
                        albumFeatureApi = albumFeatureApi,
                        authFeatureApi = authFeatureApi,
                    )

                }
            }
        }
    }
}


private const val TAG = "AppContent"