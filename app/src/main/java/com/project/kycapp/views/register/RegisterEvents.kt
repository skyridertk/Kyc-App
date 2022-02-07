package com.project.kycapp.views.register

sealed class RegisterEvents {
    object Register: RegisterEvents()
    data class ChangeEmail(var value: String): RegisterEvents()
    data class ChangePassword(var value: String): RegisterEvents()
    object Login: RegisterEvents()
}