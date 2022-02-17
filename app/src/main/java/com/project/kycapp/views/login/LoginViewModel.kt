package com.project.kycapp.views.login
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.kycapp.data.datastore.UserPreferences
import com.project.kycapp.data.repository.Resource
import com.project.kycapp.models.Account
import com.project.kycapp.repository.KycRepository
import com.project.kycapp.views.register.RegisterViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val kycRepository: KycRepository, private val userPreferences: UserPreferences) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState>
        get() = _state

    companion object {
        const val TAG = "RegisterViewModel"
    }

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(events: LoginEvents){
        when (events) {
            is LoginEvents.Login -> {
                viewModelScope.launch {
                    try {
                        val account = Account(state.value.email, state.value.password)

                        _state.value = state.value.copy(
                            isLoading = true
                        )

                        Log.d(TAG, "onEvent: $account")

                        kycRepository.login(account).collect {

                            when (it){
                                is Resource.Error -> {
                                    _eventFlow.emit(UIEvent.Error(it.message ?: ""))
                                }
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
            is LoginEvents.ChangeEmail -> {
                _state.value = state.value.copy(
                    email = events.value
                )
            }
            is LoginEvents.ChangePassword -> {
                _state.value = state.value.copy(
                    password = events.value
                )
            }

            LoginEvents.Register -> {
                viewModelScope.launch {
                    _eventFlow.emit(UIEvent.Register)
                }
            }
        }
    }

    sealed class UIEvent {
        object Main: UIEvent()
        object Register: UIEvent()
        data class Error(
            val message: String
        ) : UIEvent()
    }
}