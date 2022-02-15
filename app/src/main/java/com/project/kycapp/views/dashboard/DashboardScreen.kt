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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.project.kycapp.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
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

    val scope = rememberCoroutineScope()

    val state = rememberScaffoldState()
    Scaffold(scaffoldState = state, topBar = {
        TopAppBar(
            title = {
                Text("Dashboard", color = MaterialTheme.colors.primary)
            },
            actions = {
                IconButton(onClick = { expandedState = true }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_expand_more_24),
                        contentDescription = null,
                        tint = Color.White
                    )

                    DropdownMenu(
                        expanded = expandedState,
                        onDismissRequest = { expandedState = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            expandedState = false
                            viewModel.onEvent(DashboardEvents.Logout)
                        }) {
                            Text(text = "Logout", color = MaterialTheme.colors.primary)
                        }
                    }
                }
            },
            backgroundColor = MaterialTheme.colors.background,
            elevation = 20.dp
        )
    }) {
        DashboardDetail(viewModelState.wallet) {
            scope.launch {
                viewModel.onEvent(it)
            }
        }
    }
}

@Composable
private fun DashboardDetail(
    wallet: String,
    onClick: (DashboardEvents) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (container, walletContainer) = createRefs()

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 16.dp)
                .constrainAs(container) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        ) {

            Column(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                DashboardItem(title = "Browse", onClick = {
                    onClick(DashboardEvents.Browse)
                })
                DashboardItem(title = "Submit", onClick = {
                    onClick(DashboardEvents.Submit)
                })
            }

        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(walletContainer) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 16.dp)
                }
        ) {
            Text(
                "Kyc Wallet",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primaryVariant
            )
            Text(
                "$wallet",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.primaryVariant
            )
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
                .height(160.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onClick(title.lowercase(Locale.getDefault()))
                        }
                    )
                },
        ) {
            Row(
                modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf<Color>(
                                Color(0xFFca8a04),
                                Color(0xFFdc2626)
                            )
                        )
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "$title",
                    style = MaterialTheme.typography.h6,
                    color = Color.Black
                )
            }
        }
        Spacer(modifier = modifier.size(20.dp))
    }
}

@Preview
@Composable
fun RenderDashboard() {
    MaterialTheme {
        Surface {
            DashboardDetail(wallet = "", onClick = {})
        }
    }
}