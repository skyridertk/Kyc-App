package com.project.kycapp.views.detail

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.project.kycapp.R


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DetailScreen(
    navHostController: NavHostController = rememberAnimatedNavController(),
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    val viewModelState by detailViewModel.state.collectAsState()

    val state = rememberScaffoldState()
    Scaffold(scaffoldState = state, topBar = {
        TopAppBar(
            title = {
                Text("Kyc")
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
            Text(text = "idNumber - ${viewModelState.idNumber}")
            Text(text = "fname - ${viewModelState.firstName}")
            Text(text = "lname - ${viewModelState.surname}")
            Text(text = "phone - ${viewModelState.phoneNumber}")
            Text(text = "address - ${viewModelState.address}")
            Text(text = "status - ${viewModelState.status}")
            Text(text = "gender - ${viewModelState.gender}")
            Text(text = "dob - ${viewModelState.dateOfBirth}")
            Text(text = "approvalCount - ${viewModelState.approvalCount}")
            Text(text = "owner - ${viewModelState.owner}")
        }
    }

}