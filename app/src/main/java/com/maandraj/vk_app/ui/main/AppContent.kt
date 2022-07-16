package com.maandraj.vk_app.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.core.data.config.ConfigRepo
import com.maandraj.vk_app.ui.theme.VKAppTheme


@Composable
fun AppContent(
    authFeatureApi: AuthFeatureApi,
    configRepo: ConfigRepo
) {
    ProvideWindowInsets {
        VKAppTheme() {
            val navController = rememberNavController()
            Scaffold(
            ) { innerPaddingModifier ->
                if (!configRepo.checkTimeToken()) {
                    val startDestination = authFeatureApi.route()
                    SecurityGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPaddingModifier),
                        authFeatureApi = authFeatureApi,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}