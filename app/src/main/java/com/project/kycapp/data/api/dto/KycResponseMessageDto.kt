package com.project.kycapp.data.api.dto

import com.project.kycapp.models.Message
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KycResponseMessageDto(
    @Json(name = "message")
    val message: String
){
    fun toMessage(): Message {
        return Message(message)
    }
}
