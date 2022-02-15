package com.project.kycapp.views.register

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieConstants
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.project.kycapp.R
import com.project.kycapp.views.browse.Loader
import com.project.kycapp.views.login.CustomField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegistrationScreen(
    navHostController: NavHostController = rememberAnimatedNavController(),
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val state = rememberScaffoldState()

    val registerState by registerViewModel.state.collectAsState()

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        registerViewModel.eventFlow.collect {
            when (it) {
                RegisterViewModel.UIEvent.Main -> {
                    navHostController.navigate("main") {
                        popUpTo("main")
                        launchSingleTop = true
                    }
                }
                RegisterViewModel.UIEvent.Login -> {
                    navHostController.navigate("login") {
                        popUpTo("login")
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Scaffold(scaffoldState = state) {
        RegisterDetail(registerState.email, registerState.password, registerState.isLoading, registerState.error, registerState.errorMessage) {
            scope.launch {
                registerViewModel.onEvent(it)
            }
        }
    }
}

@Composable
private fun RegisterDetail(
    email: String,
    password: String,
    isLoading: Boolean,
    error: Boolean,
    errorMessage: String,
    onClick: (RegisterEvents) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (container, progressbar, titleBox) = createRefs()

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .constrainAs(container) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                CustomField(value = email, label = "Email", onchange = {
                    onClick(RegisterEvents.ChangeEmail(it))
                })

                Spacer(modifier = Modifier.size(20.dp))

                CustomField(value = password, label = "Password", onchange = {
                    onClick(RegisterEvents.ChangePassword(it))
                })
            }

            Spacer(modifier = Modifier.size(20.dp))

            Button(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)), onClick = {
                onClick(RegisterEvents.Register)
            }) {
                Text(text = "Register")
            }

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already have an account", color = MaterialTheme.colors.primaryVariant)
                Spacer(modifier = Modifier.size(20.dp))
                Text(text = "Login", Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onClick(RegisterEvents.Login)
                        }
                    )
                }, color = MaterialTheme.colors.primaryVariant)
            }
        }

        Box(modifier = Modifier
            .constrainAs(progressbar) {
                top.linkTo(container.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, 12.dp)
                width= Dimension.fillToConstraints
            }) {
            when {
                isLoading -> {
                    Loader(
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.Center),
                        R.raw.spinner,
                        LottieConstants.IterateForever
                    )
                }
                error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center),
                    ){
                        Loader(
                            modifier = Modifier
                                .size(150.dp),
                            R.raw.error,
                            LottieConstants.IterateForever
                        )

//                        Text(text=errorMessage, colors = MaterialTheme.colors.primary)
                    }
                }
                else -> {
                    Box{}
                }
            }
        }

        Column(
            modifier = Modifier.constrainAs(titleBox) {
                bottom.linkTo(container.top)
                start.linkTo(container.start)
                end.linkTo(container.end)
                top.linkTo(parent.top)
            },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Kyc App",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h3
            )
            Text("Register an Account", color = MaterialTheme.colors.primaryVariant)
        }
    }
}

@Preview
@Composable
fun RenderSignup() {
    MaterialTheme {
        Surface {
            RegisterDetail(email = "", password = "", isLoading = false, onClick = {}, error=false, errorMessage="")
        }
    }
}

@Preview
@Composable
fun RenderSignupLoading() {
    MaterialTheme {
        Surface {
            RegisterDetail(email = "", password = "", isLoading = true, onClick = {}, error=false, errorMessage="")
        }
    }
}

@Preview
@Composable
fun RenderSignupError() {
    MaterialTheme {
        Surface {
            RegisterDetail(email = "", password = "", isLoading = false, onClick = {}, error=true, errorMessage="Just an error")
        }
    }
}

@Preview
@Composable
fun RenderSignupBack() {
    MaterialTheme {
        Surface {
            Scaffold(backgroundColor = Color(0xFF121B2E)) {
                RegisterDetail(email = "", password = "", isLoading = true, onClick = {}, error=false, errorMessage="")
            }
        }
    }

}