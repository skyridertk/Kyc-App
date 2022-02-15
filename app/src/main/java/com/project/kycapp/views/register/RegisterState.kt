package com.project.kycapp.views.register

data class RegisterState(
    var email: String = "",
    var password: String = "",
    var isLoading: Boolean = false,
    var error: Boolean = false,
    var errorMessage: String = ""
)
