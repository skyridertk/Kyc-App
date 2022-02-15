package com.project.kycapp.views.browse

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.project.kycapp.R
import com.project.kycapp.models.Kyc
import com.project.kycapp.models.Pending
import com.project.kycapp.views.dashboard.DashboardEvents
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BrowseScreen(
    navHostController: NavHostController = rememberAnimatedNavController(),
    browseViewModel: BrowseViewModel = hiltViewModel()
) {

    val viewModelState by browseViewModel.state.collectAsState()

    val scope = rememberCoroutineScope()

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

                    navHostController.navigate("detail/$userJson") {
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
                Text("Browse", color = MaterialTheme.colors.primary)
            },
            navigationIcon = {
                IconButton(onClick = {
                    navHostController.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            backgroundColor = MaterialTheme.colors.background,
            elevation = 2.dp
        )
    }) {
        BrowseDetail(viewModelState.listOfKyc, viewModelState.isLoading, viewModelState.error) {
            scope.launch {
                browseViewModel.onEvent(it)
            }
        }
    }
}

@Composable
private fun BrowseDetail(
    listOfKyc: List<Kyc>,
    isLoading: Boolean,
    isError: Boolean,
    onClick: (BrowseEvents) -> Unit
) {
    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Loader(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center),
                    R.raw.spinner,
                    LottieConstants.IterateForever
                )
            }
        }
        isError -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Loader(
                        modifier = Modifier
                            .size(300.dp),
                        R.raw.error,
                        LottieConstants.IterateForever
                    )
                    Text(
                        text = "Error",
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }

        listOfKyc.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Loader(
                        modifier = Modifier
                            .size(300.dp),
                        R.raw.emptybox
                    )
                    Text(
                        text = "No Data",
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }

        else -> {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listOfKyc) { kyc ->
                    Row(
                        Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    onClick(BrowseEvents.OpenKyc(kyc))
                                }
                            )
                        }
                    ) {
                        RowItem(kyc.id, kyc.status)
                    }
                }
            }
        }
    }
}

@Composable
fun RowItem(id: String, status: Pending) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray)
            .padding(horizontal = 16.dp)
            .height(100.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(id, color = MaterialTheme.colors.primary, style = MaterialTheme.typography.h5)
        when (status) {
            Pending.PENDING -> {
                BoxState(text = "P", Color(0xFF87ceeb))
            }
            Pending.APPROVED -> {
                BoxState(text = "A", Color(0xFF90EE90))
            }
            Pending.REJECTED -> {
                BoxState(text = "R", Color(0xFFFF7F7F))
            }
        }
    }
}

@Composable
fun BoxState(text: String, color: Color = Color.LightGray, textColor: Color = Color.Black) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(color)
    ) {
        Text("$text", color = textColor, modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun Loader(modifier: Modifier, raw: Int, iterations: Int = 1) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(raw))
    LottieAnimation(composition, modifier = modifier, iterations = iterations)
}

@Preview
@Composable
fun BrowsePreview() {
    MaterialTheme {
        Surface {
            Sample()
        }
    }
}

@Composable
private fun Sample() {
    Column {
        RowItem(id = "1", status = Pending.PENDING)
        RowItem(id = "1", status = Pending.APPROVED)
        RowItem(id = "1", status = Pending.REJECTED)
    }
}

@Preview
@Composable
fun RenderBrowser() {
    val state = BrowseState(isLoading = true)
    MaterialTheme {
        Surface {
            BrowseDetail(
                listOfKyc = state.listOfKyc,
                isLoading = state.isLoading,
                isError = state.error,
                onClick = {})
        }
    }
}