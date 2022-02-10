package com.project.kycapp.views.login

sealed class LoginEvents {
    object Login: LoginEvents()
    data class ChangeEmail(var value: String): LoginEvents()
    data class ChangePassword(var value: String): LoginEvents()
    object Register: LoginEvents()
}