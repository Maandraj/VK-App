package com.maandraj.auth_impl.presentation.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.auth_impl.di.AuthScreenDepsProvider
import com.maandraj.auth_impl.di.DaggerAuthComponent
import com.maandraj.auth_impl.presentation.authScreen.AuthScreen
import com.maandraj.core.route.AUTH_ROUTE
import com.maandraj.core.utils.daggerViewModel

class AuthFeatureImpl : AuthFeatureApi {
    override fun route(): String = AUTH_ROUTE

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier,
    ) {
        navGraphBuilder.composable(AUTH_ROUTE)
        {

            val viewModel = daggerViewModel {
                remember {
                    DaggerAuthComponent.builder()
                        .authScreenDeps(AuthScreenDepsProvider.deps)
                        .build().getViewModel()
                }
            }
            AuthScreen(navController = navController, modifier = modifier, viewModel =  viewModel )
        }
    }
}