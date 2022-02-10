package com.project.kycapp.views.login

data class LoginState(
    val email: String="",
    val password: String="",
    var isLoading: Boolean = false
)
