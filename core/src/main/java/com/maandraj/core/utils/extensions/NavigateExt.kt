package com.maandraj.core.utils.extensions

import androidx.navigation.NavController

fun NavController.popSaveStateNavigation(route: String, startDestinationId: Int, save:Boolean = true) {
    navigate(route) {
        popUpTo(startDestinationId) {
            saveState = save
        }
        launchSingleTop = true
        restoreState = save
    }
}

