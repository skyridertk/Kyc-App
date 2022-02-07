package com.project.kycapp.data.api.dto


import com.project.kycapp.models.Validation
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ValidationDto(
    @Json(name = "message")
    val message: String,
    @Json(name = "state")
    val state: Boolean,
    @Json(name = "wallet")
    val wallet: String
){
    fun toValidation(): Validation {
        return Validation(
            message, state, wallet
        )
    }
}