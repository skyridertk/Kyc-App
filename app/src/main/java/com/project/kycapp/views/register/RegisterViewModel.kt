package com.project.kycapp.views.register


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.kycapp.data.datastore.UserPreferences
import com.project.kycapp.data.repository.Resource
import com.project.kycapp.models.Account
import com.project.kycapp.repository.KycRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val kycRepository: KycRepository, private val userPreferences: UserPreferences) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState>
        get() = _state

    companion object {
        const val TAG = "RegisterViewModel"
    }

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(events: RegisterEvents){
        when (events) {
            is RegisterEvents.Register -> {
                viewModelScope.launch {
                    try {
                        val account = Account(state.value.email, state.value.password)

                        _state.value = state.value.copy(
                            isLoading = true
                        )

                        Log.d(TAG, "onEvent: $account")

                        kycRepository.register(account).collect {

                            when (it){
                                is Resource.Error -> TODO()
                                is Resource.Success -> {
                                    Log.d(TAG, "onEvent: DATA ${it.message}")

                                    it.data?.token?.let { it1 -> userPreferences.setToken(it1) }

                                    _eventFlow.emit(UIEvent.Main)
                                }
                            }

                        }

                    } catch (ex: Exception){
                        Log.d(TAG, "onEvent: Exception ${ex.localizedMessage}")
                    }
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

            RegisterEvents.Login -> {
                viewModelScope.launch {
                    _eventFlow.emit(UIEvent.Login)
                }
            }
        }
    }

    sealed class UIEvent {
        object Main: UIEvent()
        object Login: UIEvent()
    }
}