package com.project.kycapp.views.dashboard

sealed class DashboardEvents {
    data class VerifyToken(val value: String): DashboardEvents()
    object Cleanup: DashboardEvents()
    object Browse: DashboardEvents()
    object Submit: DashboardEvents()
}
