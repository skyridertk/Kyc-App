package com.project.kycapp

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.project.kycapp.views.browse.BrowseScreen
import com.project.kycapp.views.dashboard.DashboardScreen
import com.project.kycapp.views.detail.DetailScreen
import com.project.kycapp.views.login.LoginScreen
import com.project.kycapp.views.register.RegistrationScreen
import com.project.kycapp.views.submit.SubmitScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationController(startDestination: String = "authentication") {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(navController = navController, startDestination = startDestination) {
        navigation(startDestination = "register", route = "authentication") {
            composable("register") {
                RegistrationScreen(navController)
            }

            composable("login") {
                LoginScreen(navController)
            }
        }

        navigation(startDestination = "dashboard", route = "main") {
            composable("dashboard") {
                DashboardScreen(navController)
            }

            composable("browse") {
                BrowseScreen(navController)
            }

            composable(
                "detail/{data}",
                arguments = listOf(navArgument("data") {
                    type = NavType.StringType
                })
            ) {
                DetailScreen(navController)
            }

            composable("submit") {
                SubmitScreen(navController)
            }
        }
    }


}
