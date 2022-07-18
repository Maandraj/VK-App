package com.maandraj.vk_app.ui.main.content

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.core.data.result.ResultOf
import com.maandraj.vk_app.ui.main.activity.MainActivityVM
import com.maandraj.vk_app.ui.main.graph.SafeGraph
import com.maandraj.vk_app.ui.theme.VKAppTheme
import com.maandraj.vk_app.utils.NavigationGraph


@Composable
fun AppContent(
    authFeatureApi: AuthFeatureApi,
    viewModel: MainActivityVM,
) {
    ProvideWindowInsets {
        VKAppTheme() {
            val navController = rememberNavController()
            Scaffold(
            ) { innerPaddingModifier ->
                val isLogged = viewModel.isLogged()
                if (isLogged) {
                    when (viewModel.isSeanceNotExpired()) {
                        is ResultOf.Success -> {
                            SetGraph(NavigationGraph.SAFE,
                                navController = navController,
                                authFeatureApi = authFeatureApi,
                                innerPaddingModifier = innerPaddingModifier,
                                viewModel = viewModel)
                        }
                        is ResultOf.Failure -> {
                            SetGraph(NavigationGraph.UNSAFE,
                                navController = navController,
                                authFeatureApi = authFeatureApi,
                                innerPaddingModifier = innerPaddingModifier,
                                viewModel = viewModel)
                        }
                    }
                } else {
                    SetGraph(NavigationGraph.UNSAFE,
                        navController = navController,
                        authFeatureApi = authFeatureApi,
                        innerPaddingModifier = innerPaddingModifier,
                        viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun SetGraph(
    navigationGraph: NavigationGraph,
    navController: NavHostController,
    authFeatureApi: AuthFeatureApi,
    innerPaddingModifier: PaddingValues,
    viewModel: MainActivityVM,
) {
    when (navigationGraph) {
        NavigationGraph.SAFE -> {
            navController.clearBackStack(route = authFeatureApi.route())
            Log.i(TAG, "Safe mode")
        }
        NavigationGraph.UNSAFE -> {
            viewModel.logout()
            val startDestination = authFeatureApi.route()
            SafeGraph(
                navController = navController,
                modifier = Modifier.padding(innerPaddingModifier),
                authFeatureApi = authFeatureApi,
                startDestination = startDestination
            )
        }
    }

}

private const val TAG = "AppContent"