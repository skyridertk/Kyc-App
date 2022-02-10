package com.project.kycapp.views.browse

import com.project.kycapp.models.Kyc

data class BrowseState(
    val listOfKyc: List<Kyc> = emptyList(),
    val error: Boolean = false,
    val isLoading: Boolean = false,
    val token: String = ""
)