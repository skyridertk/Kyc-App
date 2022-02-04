package com.project.kycapp

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable

import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.project.kycapp.views.register.RegistrationScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationController() {
    val navController = rememberAnimatedNavController()
    
    val TAG = "NavigationController"

    AnimatedNavHost(navController = navController, startDestination = "registration"){
        navigation(startDestination = "register", route = "registration"){
            Log.d(TAG, "NavigationController: Here")
            Log.d(TAG, "NavigationController: ${this.route}")
            Log.d(TAG, "NavigationController: ${this.label}")
            Log.d(TAG, "NavigationController: ${this.provider.navigators}")
            Log.d(TAG, "NavigationController: ${this.toString()}")
            Log.d(TAG, "NavigationController: ")
            composable("register"){
                Log.d(TAG, "NavigationController: Inside")
                RegistrationScreen(navController)
            }
        }
    }
    
    
}