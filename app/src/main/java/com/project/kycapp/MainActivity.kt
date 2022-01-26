package com.project.kycapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.project.kycapp.ui.theme.KycAppTheme
import com.project.kycapp.views.login.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KycAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    LoginScreen()
                }
            }
        }
    }
}

