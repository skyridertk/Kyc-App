package com.project.kycapp

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
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
            composable("register",
                enterTransition = {
                    when (initialState.destination.route) {
                        "login" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "dashboard" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        "login" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "dashboard" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        else -> null
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        "login" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "dashboard" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        else -> null
                    }
                },
                popExitTransition = {
                    when (targetState.destination.route) {
                        "login" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "dashboard" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        else -> null
                    }
                }
                ) {
                RegistrationScreen(navController)
            }

            composable("login",
                enterTransition = {
                    when (initialState.destination.route) {
                        "register" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "dashboard" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        "register" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "dashboard" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        else -> null
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        "register" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "dashboard" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        else -> null
                    }
                },
                popExitTransition = {
                    when (targetState.destination.route) {
                        "register" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "dashboard" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        else -> null
                    }
                }
                ) {
                LoginScreen(navController)
            }
        }

        navigation(startDestination = "dashboard", route = "main") {
            composable("dashboard",
                enterTransition = {
                    when (initialState.destination.route) {
                        "login" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "register" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "dashboard" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "browse" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "submit" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        "login" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "register" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "dashboard" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "browse" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "submit" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        else -> null
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        "login" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "register" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "dashboard" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "browse" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "submit" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))

                        else -> null
                    }
                },
                popExitTransition = {
                    when (targetState.destination.route) {
                        "login" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "dashboard" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "register" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "browse" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "submit" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        else -> null
                    }
                }
                ) {
                DashboardScreen(navController)
            }

            composable("browse",
                enterTransition = {
                    when (initialState.destination.route) {
                        "detail" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "dashboard" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        "detail" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        "dashboard" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                        else -> null
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        "detail" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "dashboard" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        else -> null
                    }
                },
                popExitTransition = {
                    when (targetState.destination.route) {
                        "detail" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        "dashboard" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                        else -> null
                    }
                }
                ) {
                BrowseScreen(navController)
            }

            composable(
                "detail/{data}",
                arguments = listOf(navArgument("data") {
                    type = NavType.StringType
                }),
                enterTransition = {
                    when (initialState.destination.route) {
                        "browse" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))

                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        "browse" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))

                        else -> null
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        "browse" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))

                        else -> null
                    }
                },
                popExitTransition = {
                    when (targetState.destination.route) {
                        "browse" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))

                        else -> null
                    }
                }
            ) {
                DetailScreen(navController)
            }

            composable("submit",
                enterTransition = {
                    when (initialState.destination.route) {
                        "dashboard" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))

                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        "dashboard" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))

                        else -> null
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        "dashboard" ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))

                        else -> null
                    }
                },
                popExitTransition = {
                    when (targetState.destination.route) {
                        "dashboard" ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))

                        else -> null
                    }
                }
                ) {
                SubmitScreen(navController)
            }
        }
    }


}
