package com.project.kycapp.views.browse

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
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
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BrowseScreen(
    navHostController: NavHostController = rememberAnimatedNavController(),
    browseViewModel: BrowseViewModel = hiltViewModel()
) {

    val viewModelState by browseViewModel.state.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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

                    val encodedUrl = URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())

                    navHostController.navigate("detail/$encodedUrl") {
                        popUpTo("browse")
                        launchSingleTop = true
                    }
                }
                is BrowseViewModel.UIEvent.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


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
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(listOfKyc) { index, kyc ->
                    Row(
                        Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    onClick(BrowseEvents.OpenKyc(kyc))
                                }
                            )
                        }
                    ) {
                        val color = when (kyc.status) {
                            Pending.PENDING -> {
                                Color(0xFF87ceeb)
                            }
                            Pending.APPROVED -> {
                                Color(0xFF90EE90)
                            }
                            else -> {
                                Color(0xFFFF7F7F)
                            }
                        }
                        RowItem(kyc.id, kyc.status, color, index)
                    }
                }
            }
        }
    }
}

@Composable
fun RowItem(id: String, status: Pending, color: Color, index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .padding(horizontal = 16.dp)
            .height(100.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1F),
            ) {
                Text(
                    text = "$index",
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h5
                )
            }

            Text(
                modifier = Modifier.weight(3F),
                text = id,
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h5
            )
        }
    }
}

@Composable
fun BoxState(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.LightGray,
    textColor: Color = Color.Black
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(color)
    ) {
        Text("$text", color = textColor, modifier = modifier.align(Alignment.Center))
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
        RowItem(id = "1", status = Pending.PENDING, Color.Blue, 1)
        RowItem(id = "1", status = Pending.APPROVED, Color.Blue, 2)
        RowItem(id = "1", status = Pending.REJECTED, Color.Blue, 3)
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