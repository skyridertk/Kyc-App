package com.project.kycapp.views.browse

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.project.kycapp.R
import com.project.kycapp.models.Kyc
import com.project.kycapp.views.dashboard.DashboardEvents
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BrowseScreen(
    navHostController: NavHostController = rememberAnimatedNavController(),
    browseViewModel: BrowseViewModel = hiltViewModel()
) {

    val viewModelState by browseViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        browseViewModel.onEvent(BrowseEvents.FETCH)

        browseViewModel.eventFlow.collect {
            when (it) {
                is BrowseViewModel.UIEvent.OpenDetail -> {
                    val moshi = Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                    val jsonAdapter = moshi.adapter(Kyc::class.java).lenient()
                    val userJson = jsonAdapter.toJson(it.kyc)

                    navHostController.navigate("detail/$userJson"){
                        popUpTo("browse")
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Log.d("BrowseScreen", "BrowseScreen: ${viewModelState.listOfKyc}")

    val state = rememberScaffoldState()
    Scaffold(scaffoldState = state, topBar = {
        TopAppBar(
            title = {
                Text("User Kyc")
            },
            navigationIcon = {
                IconButton(onClick = {
                    navHostController.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        contentDescription = null
                    )
                }
            },
            backgroundColor = Color.White,
            elevation = 2.dp
        )
    }) {
        Column {
            LazyColumn {
                items(viewModelState.listOfKyc) { kyc ->
                    Row(
                        Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    browseViewModel.onEvent(BrowseEvents.OpenKyc(kyc))
                                }
                            )
                        }
                    ) {
                        Text(text = kyc.firstName)
                    }
                }
            }
        }
    }
}