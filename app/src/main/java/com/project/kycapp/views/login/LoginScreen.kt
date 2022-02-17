package com.project.kycapp.views.login

import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {

    val registerState by loginViewModel.state.collectAsState()

    val scope = rememberCoroutineScope()

    val current = LocalContext.current

    val state = rememberScaffoldState()

    LaunchedEffect(Unit) {
        loginViewModel.eventFlow.collect {
            when (it) {
                LoginViewModel.UIEvent.Main -> {
                    navHostController.navigate("main") {
                        popUpTo("main")
                        launchSingleTop = true
                    }
                }

                LoginViewModel.UIEvent.Register -> {
                    navHostController.navigate("register") {
                        popUpTo("register")
                        launchSingleTop = true
                    }
                }
                is LoginViewModel.UIEvent.Error -> {
                    Toast.makeText(current, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(scaffoldState = state) {
        LoginDetail(registerState.email, registerState.password, registerState.isLoading) {
            scope.launch {
                loginViewModel.onEvent(it)
            }
        }
    }
}

@Composable
private fun LoginDetail(
    email: String,
    password: String,
    isLoading: Boolean,
    onClick: (LoginEvents) -> Unit
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
                    onClick(LoginEvents.ChangeEmail(it))
                })

                Spacer(modifier = Modifier.size(20.dp))

                CustomField(value = password, label = "Password", onchange = {
                    onClick(LoginEvents.ChangePassword(it))
                })
            }

            Spacer(modifier = Modifier.size(20.dp))

            Button(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)), onClick = {
                onClick(LoginEvents.Login)
            }) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Are you new?", color = MaterialTheme.colors.primaryVariant)
                Spacer(modifier = Modifier.size(20.dp))
                Text(text = "Register", Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onClick(LoginEvents.Register)
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
            }
            .fillMaxWidth()) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center),
                    color = Color.Gray,
                    strokeWidth = 8.dp
                )
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
            Text("Connect to your account", color = MaterialTheme.colors.primaryVariant)
        }
    }
}

@Composable
fun CustomField(value: String, label: String, onchange: (String) -> Unit) {
    TextField(value = "$value", onValueChange = {
        onchange(it)
    }, label = {
        Text("$label")
    }, modifier = Modifier
        .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.LightGray,
            cursorColor = MaterialTheme.colors.background,
            disabledLabelColor = Color.LightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            placeholderColor = MaterialTheme.colors.background,
            focusedLabelColor = MaterialTheme.colors.background
        )
    )
}