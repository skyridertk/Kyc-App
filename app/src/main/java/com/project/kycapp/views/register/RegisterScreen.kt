package com.project.kycapp.views.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import com.project.kycapp.views.login.CustomField

@Composable
fun RegistrationScreen() {
    val state = rememberScaffoldState()
    
    Scaffold(scaffoldState = state) {
        Column {
            Column {
                CustomField(value = "", label = "First Name", onchange = {})
                CustomField(value = "", label = "Middle Name", onchange = {})
                CustomField(value = "", label = "Last Name", onchange = {})
                CustomField(value = "", label = "ID Number", onchange = {})
                CustomField(value = "", label = "Dob", onchange = {})
                CustomField(value = "", label = "Gender", onchange = {})
                CustomField(value = "", label = "Address", onchange = {})
                CustomField(value = "", label = "Password", onchange = {})
            }
            
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Proceed")
            }
        }
    }
}