package com.project.kycapp.views.submit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.kycapp.data.datastore.UserPreferences
import com.project.kycapp.data.repository.Resource
import com.project.kycapp.models.Gender
import com.project.kycapp.models.Kyc
import com.project.kycapp.models.Pending
import com.project.kycapp.repository.KycRepository
import com.project.kycapp.views.login.LoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubmitViewModel @Inject constructor(
    userPreferences: UserPreferences,
    val kycRepository: KycRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SubmitState())
    val state: StateFlow<SubmitState>
        get() = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            userPreferences.tokenFlow().collect {
                _state.value = state.value.copy(
                    token = it
                )
            }
        }
    }

    fun onEvent(events: SubmitEvents) {
        when (events) {
            is SubmitEvents.ChangeAddress -> {
                _state.value = state.value.copy(
                    address = events.value
                )
            }
            is SubmitEvents.ChangeDateOfBirth -> {
                _state.value = state.value.copy(
                    dateOfBirth = events.value
                )
            }
            is SubmitEvents.ChangeFirstName -> {
                _state.value = state.value.copy(
                    firstName = events.value
                )
            }
            is SubmitEvents.ChangeGender -> {
                _state.value = state.value.copy(
                    gender = events.value
                )
            }
            is SubmitEvents.ChangeIdNumber -> {
                _state.value = state.value.copy(
                    idNumber = events.value
                )
            }
            is SubmitEvents.ChangePhoneNumber -> {
                _state.value = state.value.copy(
                    phoneNumber = events.value
                )
            }
            is SubmitEvents.ChangeSurname -> {
                _state.value = state.value.copy(
                    surname = events.value
                )
            }
            SubmitEvents.Submit -> {
                viewModelScope.launch {
                    if (state.value.proofIdPreview || state.value.proofIdPreview){
                        _eventFlow.emit(UIEvent.Error("Please wait for preview"))
                    } else{

                        try {
                            val kyc = Kyc(
                                id = "0",
                                idNumber = state.value.idNumber,
                                firstName = state.value.firstName,
                                surname = state.value.surname,
                                phoneNumber = state.value.phoneNumber,
                                address = state.value.address,
                                status = state.value.status,
                                approvalCount = state.value.approvalCount,
                                dateOfBirth = state.value.dateOfBirth,
                                owner = state.value.owner,
                                gender = state.value.gender,
                                proofOfId = state.value.proofOfId,
                                proofOfResidence = state.value.proofOfResidence
                            )

                            kycRepository.submit(state.value.token, kyc.toKycRequestDto()).collect {
                                when (it) {
                                    is Resource.Error -> {

                                        _state.value = state.value.copy(
                                            loading = false
                                        )

                                        _eventFlow.emit(UIEvent.Error(it.message ?: ""))
                                    }
                                    is Resource.Success -> {

                                        _state.value = state.value.copy(
                                            loading = false
                                        )

                                        _eventFlow.emit(UIEvent.Main)
                                    }
                                    is Resource.Loading -> {
                                        _state.value = state.value.copy(
                                            loading = true
                                        )
                                    }
                                }
                            }

                        } catch (ex: Exception) {
                            _eventFlow.emit(UIEvent.Error(ex.localizedMessage ?: ""))
                        }
                    }
                }
            }
            is SubmitEvents.ChangeProofOfId -> {
                _state.value = state.value.copy(
                    proofOfId = events.value
                )
            }
            is SubmitEvents.ChangeProofOfResidence -> {

                _state.value = state.value.copy(
                    proofOfResidence = events.value
                )
            }
            is SubmitEvents.Clear -> {
                _state.value = state.value.copy(
                    token = "",
                    idNumber = "",
                    firstName = "",
                    surname = "",
                    phoneNumber = "",
                    address = "",
                    dateOfBirth = "",
                    gender = Gender.FEMALE,
                    approvalCount = 0,
                    owner = "",
                    status = Pending.PENDING,
                    proofOfResidence = "",
                    proofOfId = "",
                    face = "",
                    loading = false
                )
            }
            is SubmitEvents.ChangeIDPreview -> {
                _state.value = state.value.copy(
                    proofIdPreview = events.value
                )
            }

            is SubmitEvents.ChangeResidencePreview -> {
                _state.value = state.value.copy(
                    proofResPreview = events.value
                )
            }
        }
    }

    sealed class UIEvent {
        object Main : UIEvent()
        data class Error(val message: String) : UIEvent()
    }
}