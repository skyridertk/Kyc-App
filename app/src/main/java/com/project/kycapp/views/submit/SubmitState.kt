package com.project.kycapp.views.submit

import com.project.kycapp.models.Gender
import com.project.kycapp.models.Pending

data class SubmitState(
    val token: String = "",
    val idNumber: String = "",
    val firstName: String = "",
    val surname: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val dateOfBirth: String = "",
    val gender: Gender = Gender.FEMALE,
    val approvalCount: Int = 0,
    val owner: String = "",
    val status: Pending = Pending.PENDING
)
