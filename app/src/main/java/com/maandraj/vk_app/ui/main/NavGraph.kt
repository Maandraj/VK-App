package com.maandraj.vk_app.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.feature_api.utils.register


@Composable
fun SecurityGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authFeatureApi: AuthFeatureApi,
    startDestination : String,
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
