package com.maandraj.core.utils.extensions

import androidx.navigation.NavController

fun NavController.popSaveStateNavigation(route: String, startDestinationId: Int) {
    navigate(route) {
        popUpTo(startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

