package com.project.kycapp.views.submit

import com.project.kycapp.models.Gender

sealed class SubmitEvents {
    data class ChangeIdNumber(var value: String): SubmitEvents()
    data class ChangeFirstName(var value: String): SubmitEvents()
    data class ChangeSurname(var value: String): SubmitEvents()
    data class ChangePhoneNumber(var value: String): SubmitEvents()
    data class ChangeAddress(var value: String): SubmitEvents()
    data class ChangeDateOfBirth(var value: String): SubmitEvents()
    data class ChangeGender(var value: Gender): SubmitEvents()
    data class ChangeProofOfResidence(var value: String): SubmitEvents()
    data class ChangeProofOfId(var value: String): SubmitEvents()
    object Submit: SubmitEvents()
}
