package com.maandraj.album_impl.ui.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.maandraj.album_api.AlbumFeatureApi
import com.maandraj.album_impl.di.AlbumScreenDepsProvider
import com.maandraj.album_impl.di.DaggerAlbumComponent
import com.maandraj.album_impl.ui.album.AlbumScreen
import com.maandraj.core.utils.extensions.daggerViewModel
import com.maandraj.core.utils.route.ALBUM_ROUTE
import javax.inject.Inject

class AlbumFeatureImpl @Inject constructor(
) : AlbumFeatureApi {
    override fun route(): String = ALBUM_ROUTE

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier,
    ) {
        navGraphBuilder.composable(ALBUM_ROUTE)
        {
            val deps = AlbumScreenDepsProvider.deps
            val authFeatureApi = deps.authFeatureApi
            val viewModel = daggerViewModel {
                remember {
                    DaggerAlbumComponent.builder()
                        .albumDeps(AlbumScreenDepsProvider.deps)
                        .build().getViewModel()
                }
            }
            AlbumScreen(navController = navController,
                modifier = modifier,
                viewModel = viewModel,
                authFeatureApi = authFeatureApi)
        }
    }
}