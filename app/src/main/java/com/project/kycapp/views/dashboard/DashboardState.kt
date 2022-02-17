package com.project.kycapp.views.dashboard

data class DashboardState(
    val token: String = "",
    val email: String = "",
    val wallet: String = "--------",
    val error: Boolean = false
)
