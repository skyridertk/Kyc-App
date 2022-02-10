package com.project.kycapp.views.dashboard

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.project.kycapp.R
import kotlinx.coroutines.flow.collect
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun DashboardScreen(
    navHostController: NavHostController = rememberAnimatedNavController(),
    viewModel: DashboardViewModel = hiltViewModel()
) {

    val viewModelState by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        val token = viewModel.state.value.token
        Log.d("Dashboard", "DashboardScreen: $token")
        viewModel.onEvent(DashboardEvents.VerifyToken(token))

        viewModel.eventFlow.collect {
            when (it) {
                DashboardViewModel.UIEvent.Logout -> {
                    navHostController.navigate("authentication") {
                        popUpTo("authentication")
                        launchSingleTop = true
                    }
                }
                DashboardViewModel.UIEvent.Browse -> {
                    navHostController.navigate("browse") {
                        popUpTo("dashboard")
                        launchSingleTop = true
                    }
                }
                DashboardViewModel.UIEvent.Submit -> {
                    navHostController.navigate("submit") {
                        popUpTo("dashboard")
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    var expandedState by remember {
        mutableStateOf(false)
    }

    val state = rememberScaffoldState()
    Scaffold(scaffoldState = state, topBar = {
        TopAppBar(
            title = {
                Text("Kyc App")
            },
            actions = {
                IconButton(onClick = { expandedState = true }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_expand_more_24),
                        contentDescription = null
                    )

                    DropdownMenu(
                        expanded = expandedState,
                        onDismissRequest = { expandedState = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            expandedState = false
                            viewModel.onEvent(DashboardEvents.Logout)
                        }) {
                            Text(text = "Logout")
                        }
                    }
                }
            },
            backgroundColor = Color.White,
            elevation = 2.dp
        )
    }) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 32.dp)
            ) {
                Text("Hi", style = MaterialTheme.typography.h3)
                Text("jet@gmail.com", style = MaterialTheme.typography.h5)
            }
            Spacer(modifier = Modifier.size(20.dp))
            Column(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                DashboardItem(title = "Browse", onClick = {
                    viewModel.onEvent(DashboardEvents.Browse)
                })
                DashboardItem(title = "Submit", onClick = {
                    viewModel.onEvent(DashboardEvents.Submit)
                })
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Wallet", style = MaterialTheme.typography.subtitle1)
                Text("67763746376537456374573574", style = MaterialTheme.typography.subtitle2)
            }

        }
    }
}

@Composable
fun DashboardItem(modifier: Modifier = Modifier, title: String = "", onClick: (String) -> Unit) {

    Column(
        modifier = modifier
    ) {
        Card(
            modifier
                .height(140.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Cyan)
                .padding(horizontal = 18.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onClick(title.lowercase(Locale.getDefault()))
                        }
                    )
                },

            backgroundColor = Color.Cyan,
        ) {
            Row(
                modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "$title",
                    style = MaterialTheme.typography.h6,
                )
            }
        }
        Spacer(modifier = modifier.size(20.dp))
    }
}