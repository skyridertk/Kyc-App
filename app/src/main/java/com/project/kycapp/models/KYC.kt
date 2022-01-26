package com.project.kycapp.models

data class KYC(
    val kycId: String,
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