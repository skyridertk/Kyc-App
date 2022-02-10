package com.project.kycapp.views.browse

import com.project.kycapp.models.Kyc

sealed class BrowseEvents {
    object FETCH: BrowseEvents()
    data class OpenKyc(val kyc: Kyc): BrowseEvents()
}