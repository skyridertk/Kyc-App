package com.project.kycapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.project.kycapp.ui.theme.KycAppTheme
import com.project.kycapp.views.login.LoginScreen
import com.project.kycapp.views.register.RegistrationScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value.isLoading
            }
        }

        setContent {
            val data by viewModel.state.collectAsState()

            KycAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavigationController(data.startNode)
                }
            }
        }
    }
    
    companion object {
        const val TAG = "MainActivity"
    }
}

