package com.maandraj.vk_app.ui.main.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.maandraj.album_api.AlbumFeatureApi
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.feature_api.utils.register
import com.maandraj.vk_app.utils.NavigationGraph


@Composable
fun SetGraph(
    navigationGraph: NavigationGraph,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authFeatureApi: AuthFeatureApi,
    albumFeatureApi: AlbumFeatureApi,
) {
    val startDestination = when (navigationGraph) {
        NavigationGraph.SAFE -> {
            albumFeatureApi.route()
        }
        NavigationGraph.UNSAFE -> {
            authFeatureApi.route()
        }
    }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        register(
            authFeatureApi,
            navController = navController,
            modifier = modifier
        )
        register(
            albumFeatureApi,
            navController = navController,
            modifier = modifier
        )
    }
}
