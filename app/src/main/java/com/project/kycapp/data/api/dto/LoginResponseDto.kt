package com.project.kycapp.data.api.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponseDto(
    @Json(name = "message")
    val message: String,
    @Json(name = "token")
    val token: String
)