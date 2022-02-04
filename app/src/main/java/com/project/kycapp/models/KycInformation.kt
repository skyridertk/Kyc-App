package com.project.kycapp.models

data class KycInformation(
    val kycId: String,
    val idNumber: String,
    val firstName: String,
    val middleName: String,
    val surname: String,
    val phoneNumber: String,
    val address: String,
    val dateOfBirth: String,
    val gender: Gender,
    val approvalCount: Int,
    val banks: List<Bank>,
    val owner: String,
    val documents: String
)

enum class Gender {
    MALE, FEMALE
}