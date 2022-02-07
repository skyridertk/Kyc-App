package com.project.kycapp

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable

import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.project.kycapp.views.dashboard.DashboardScreen
import com.project.kycapp.views.register.RegistrationScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationController(startDestination: String = "authentication") {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(navController = navController, startDestination = startDestination){
        navigation(startDestination = "register", route = "authentication"){
            composable("register"){
                RegistrationScreen(navController)
            }
        }

        navigation(startDestination = "dashboard", route = "main"){
            composable("dashboard"){
                DashboardScreen()
            }
        }
    }
    
    
}