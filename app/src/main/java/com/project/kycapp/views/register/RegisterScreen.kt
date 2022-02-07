package com.project.kycapp.views.register

import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.project.kycapp.views.login.CustomField
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegistrationScreen(
    navHostController: NavHostController,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val state = rememberScaffoldState()

    val registerState by registerViewModel.state.collectAsState()

    val scope = rememberCoroutineScope()

    val current = LocalContext.current

    LaunchedEffect(Unit){
        registerViewModel.eventFlow.collect {
            when (it){
                RegisterViewModel.UIEvent.Main -> {
                    navHostController.navigate("main")
                }
                RegisterViewModel.UIEvent.Login -> {
                    navHostController.navigate("login")
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
                        registerViewModel.onEvent(RegisterEvents.ChangeEmail(it))
                    })

                    Spacer(modifier = Modifier.size(20.dp))

                    CustomField(value = registerState.password, label = "Password", onchange = {
                        registerViewModel.onEvent(RegisterEvents.ChangePassword(it))
                    })
                }


                Button(onClick = {
                    scope.launch {
                        registerViewModel.onEvent(RegisterEvents.Register)
                    }
                }) {
                    Text(text = "Register")
                }

                Spacer(modifier = Modifier.size(20.dp))

                Row {
                    Text("Already have an account")
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "Login", Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                Log.d("Register", "RegistrationScreen: Navigate")
                                Toast.makeText(current, "navigate to another screen", Toast.LENGTH_LONG).show()
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