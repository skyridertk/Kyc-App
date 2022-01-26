package com.project.kycapp.views.login

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen() {
    val state = rememberScaffoldState()
    Scaffold(scaffoldState = state) {
        Column {
            Text("KYCHAIN")
            Column {
                CustomField(value = "", label = "Email Address", {})
                CustomField(value = "", label = "Password", {})
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Sign in")
                }
                Row {
                    Text(text = "Don't have a profile?")
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Sign up now")
                    }
                }
            }
        }
    }
}

@Composable
fun CustomField(value: String, label: String, onchange: (String) -> Unit){
    TextField(value = "$value", onValueChange = {
        onchange(it)
    }, label = {
        Text("$label")
    })
}