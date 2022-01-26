package com.project.kycapp.views.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable

@Composable
fun DashboardScreen() {
    val state = rememberScaffoldState()
    Scaffold(scaffoldState = state) {
        Column {
            Text(text = "Welcome user")

            Text(text = "Apply KYC")
            Text(text = "View all KYC")
        }
    }
}