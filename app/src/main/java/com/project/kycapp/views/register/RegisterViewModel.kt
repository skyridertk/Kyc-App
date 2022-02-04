package com.project.kycapp.views.register


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.kycapp.models.Account
import com.project.kycapp.repository.KycRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val kycRepository: KycRepository) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState>
        get() = _state

    fun onEvent(events: RegisterEvents){
        when (events) {
            is RegisterEvents.Register -> {
                viewModelScope.launch {
                    val account = Account(state.value.email, state.value.password)
//                    kycRepository.register(account)
                    Log.d("Account", "onEvent: $account")
                    _state.value = state.value.copy(
                        isLoading = true
                    )
                }
            }
            is RegisterEvents.ChangeEmail -> {
                _state.value = state.value.copy(
                    email = events.value
                )
            }
            is RegisterEvents.ChangePassword -> {
                _state.value = state.value.copy(
                    password = events.value
                )
            }
        }
    }
}