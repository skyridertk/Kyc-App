package com.project.kycapp.data.api.dto


import com.project.kycapp.models.Authentication
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponseDto(
    @Json(name = "message")
    val message: String,
    @Json(name = "token")
    val token: String
) {
    fun toAuthentication(): Authentication {
        return Authentication(
            message, token
        )
    }
}

