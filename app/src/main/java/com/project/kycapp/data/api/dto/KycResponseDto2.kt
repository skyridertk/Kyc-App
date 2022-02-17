package com.project.kycapp.data.api.dto


import com.project.kycapp.models.Gender
import com.project.kycapp.models.Kyc
import com.project.kycapp.models.Pending
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KycResponseDto2(
    @Json(name = "data")
    val `data`: List<Data>,
    @Json(name = "message")
    val message: String
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "Key")
        val key: String,
        @Json(name = "Record")
        val record: Record
    ) {
        @JsonClass(generateAdapter = true)
        data class Record(
            @Json(name = "address")
            val address: String,
            @Json(name = "approvalCount")
            val approvalCount: String,
            @Json(name = "assetID")
            val assetID: String,
            @Json(name = "dateOfBirth")
            val dateOfBirth: String,
            @Json(name = "firstname")
            val firstname: String,
            @Json(name = "gender")
            val gender: String,
            @Json(name = "idNumber")
            val idNumber: String,
            @Json(name = "lastname")
            val lastname: String,
            @Json(name = "owner")
            val owner: String,
            @Json(name = "proofOfId")
            val proofOfId: String,
            @Json(name = "proofOfResidence")
            val proofOfResidence: String,
            @Json(name = "status")
            val status: String
        ){
            fun toKyc(): Kyc {
                return  Kyc(
                    id = assetID,
                    idNumber =idNumber,
                    firstName = firstname,
                    surname = lastname,
                    phoneNumber = "",
                    address = address,
                    status = when (status) {
                        "approved" -> {
                            Pending.APPROVED
                        }
                        "rejected" -> {
                            Pending.REJECTED
                        }
                        else -> {
                            Pending.PENDING
                        }
                    },
                    approvalCount = approvalCount.toInt(),
                    dateOfBirth = dateOfBirth,
                    owner = owner,
                    gender = when(gender){
                        "Female" -> {
                            Gender.FEMALE
                        }

                        else -> Gender.MALE
                    },
                    proofOfResidence = proofOfResidence,
                    proofOfId = proofOfId
                )
            }
        }
    }
}