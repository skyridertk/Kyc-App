package com.project.kycapp.models

import android.os.Parcelable
import com.project.kycapp.data.api.dto.KycRequestDto
import com.squareup.moshi.JsonClass

import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
data class Kyc(
    val idNumber: String,
    val firstName: String,
    val surname: String,
    val phoneNumber: String,
    val address: String,
    val dateOfBirth: String,
    val gender: Gender,
    val approvalCount: Int,
    val owner: String,
    val status: Pending
    //val banks: List<Bank>,
    //val documents: String
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
                    "Approved"
                }
                Pending.REJECTED -> {
                    "Rejected"
                }
                else -> {
                    "Pending"
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
            }
        )
    }
}

enum class Gender {
    MALE, FEMALE
}

enum class Pending {
    PENDING, APPROVED, REJECTED
}


