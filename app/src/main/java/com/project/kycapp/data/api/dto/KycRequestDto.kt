package com.project.kycapp.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KycRequestDto(
    @Json(name = "address")
    val address: String,
    @Json(name = "approvalCount")
    val approvalCount: Int,
    @Json(name = "dob")
    val dob: String,
    @Json(name = "fname")
    val fname: String,
    @Json(name = "gender")
    val gender: String,
    @Json(name = "idNumber")
    val idNumber: String,
    @Json(name = "lname")
    val lname: String,
    @Json(name = "owner")
    val owner: String,
    @Json(name = "phone")
    val phone: String,
    @Json(name = "status")
    val status: String
)