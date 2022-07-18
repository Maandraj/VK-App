package com.maandraj.vk_app.ui.main.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.feature_api.utils.register


@Composable
fun SafeGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authFeatureApi: AuthFeatureApi,
    startDestination: String,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        register(
            authFeatureApi,
            navController = navController,
            modifier = modifier
        )
    }
}
@Composable
fun UnsafeGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
       // TODO Register screen
    }
}
