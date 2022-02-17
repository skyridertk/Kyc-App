package com.project.kycapp.models

import com.project.kycapp.data.api.dto.KycRequestDto
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Kyc(
    val id: String,
    val idNumber: String,
    val firstName: String,
    val surname: String,
    val phoneNumber: String,
    val address: String,
    val dateOfBirth: String,
    val gender: Gender,
    val approvalCount: Int,
    val owner: String,
    val status: Pending,
    val proofOfResidence: String,
    val proofOfId: String
)  {
    fun toKycRequestDto(): KycRequestDto {
        return KycRequestDto(
            idNumber = idNumber,
            fname = firstName,
            lname = surname,
            phone = phoneNumber,
            address = address,
            status = when (this@Kyc.status) {
                Pending.APPROVED -> {
                    "approved"
                }
                Pending.REJECTED -> {
                    "rejected"
                }
                else -> {
                    "pending"
                }
            },
            approvalCount = approvalCount,
            dob = dateOfBirth,
            owner = owner,
            gender = when (this@Kyc.gender) {
                Gender.FEMALE -> {
                    "Female"
                }

                else -> "Male"
            },
            proofOfResidence = proofOfResidence,
            proofOfId = proofOfId,
        )
    }
}

enum class Gender {
    MALE, FEMALE
}

enum class Pending {
    PENDING, APPROVED, REJECTED
}


