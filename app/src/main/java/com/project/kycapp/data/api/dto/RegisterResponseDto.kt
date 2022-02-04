package com.project.kycapp.data.api.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponseDto(
    @Json(name = "message")
    val message: String,
    @Json(name = "user")
    val user: User
) {
    @JsonClass(generateAdapter = true)
    data class User(
        @Json(name = "acknowledged")
        val acknowledged: Boolean,
        @Json(name = "insertedId")
        val insertedId: String
    )
}