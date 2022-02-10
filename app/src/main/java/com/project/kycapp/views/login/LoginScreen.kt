package com.project.kycapp.views.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

    LaunchedEffect(Unit){
        loginViewModel.eventFlow.collect {
            when (it){
                LoginViewModel.UIEvent.Main -> {
                    navHostController.navigate("main"){
                        popUpTo("main")
                        launchSingleTop = true
                    }
                }
                
                LoginViewModel.UIEvent.Register -> {
                    navHostController.navigate("register"){
                        popUpTo("register")
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Scaffold(scaffoldState = state) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (container, progressbar) = createRefs()

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
                    CustomField(value = registerState.email, label = "Email", onchange = {
                        loginViewModel.onEvent(LoginEvents.ChangeEmail(it))
                    })

                    Spacer(modifier = Modifier.size(20.dp))

                    CustomField(value = registerState.password, label = "Password", onchange = {
                        loginViewModel.onEvent(LoginEvents.ChangePassword(it))
                    })
                }


                Button(onClick = {
                    scope.launch {
                        loginViewModel.onEvent(LoginEvents.Login)
                    }
                }) {
                    Text(text = "Login")
                }

                Spacer(modifier = Modifier.size(20.dp))

                Row {
                    Text("Are you new?")
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "Register", Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                loginViewModel.onEvent(LoginEvents.Register)
                            }
                        )
                    }, color = Color.Blue)
                }
            }


            Box(modifier = Modifier
                .constrainAs(progressbar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 12.dp)
                }
                .fillMaxWidth()){
                if(registerState.isLoading){
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

    }
}

@Composable
fun CustomField(value: String, label: String, onchange: (String) -> Unit){
    TextField(value = "$value", onValueChange = {
        onchange(it)
    }, label = {
        Text("$label")
    }, modifier = Modifier.fillMaxWidth())
}