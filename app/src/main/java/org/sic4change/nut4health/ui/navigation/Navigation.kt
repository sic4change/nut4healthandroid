package org.sic4change.nut4health.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import coil.annotation.ExperimentalCoilApi
import org.sic4change.nut4health.ui.screens.login.LoginScreen
import org.sic4change.nut4health.ui.screens.main.MainScreen

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun Navigation(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Feature.LOGIN.route){
        composable(Feature.LOGIN.route){
            LoginScreen(onLogin = {
                navController.navigate(
                    Feature.HOME.route
                )
            })
        }
        composable(Feature.HOME.route){
            MainScreen()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
private fun NavGraphBuilder.loginNav(navController: NavController) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.LOGIN).route,
        route = Feature.LOGIN.route,
    ) {
        NavCommand.ContentType(Feature.HOME).route
        /*composable(NavCommand.ContentType(Feature.LOGIN)) {
            LoginScreen(onLogin = {
                navController.navigate(
                    NavCommand.ContentType(Feature.HOME).route
                )
                }
            )
        }*/

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
private fun NavGraphBuilder.mainNav(navController: NavController) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.HOME).route,
        route = Feature.HOME.route
    ) {

        NavCommand.ContentType(Feature.LOGIN).route

    }

}


/*
private fun NavGraphBuilder.composable(
    navItem: NavCommand,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = navItem.route,
        arguments = navItem.args,
    ) {
        content(it)
    }
}*/
