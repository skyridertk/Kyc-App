package com.project.kycapp.data.api.dto


import com.project.kycapp.models.Gender
import com.project.kycapp.models.Kyc
import com.project.kycapp.models.Pending
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KycResponseDto(
    @Json(name = "data")
    val `data`: List<Data>,
    @Json(name = "message")
    val message: String
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "id")
        val id: String,
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
    ){
        fun toKyc(): Kyc {
            return  Kyc(
                id = id,
                idNumber=idNumber,
                firstName = fname,
                surname = lname,
                phoneNumber = phone,
                address = address,
                status = when (status) {
                    "Approved" -> {
                        Pending.APPROVED
                    }
                    "Rejected" -> {
                        Pending.REJECTED
                    }
                    else -> {
                        Pending.PENDING
                    }
                },
                approvalCount = approvalCount,
                dateOfBirth = dob,
                owner = owner,
                gender = when(gender){
                    "Female" -> {
                        Gender.FEMALE
                    }

                    else -> Gender.MALE
                }
            )
        }
    }


}