package com.project.kycapp.views.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.kycapp.data.datastore.UserPreferences
import com.project.kycapp.repository.KycRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(val userPreferences: UserPreferences, val kycRepository: KycRepository) : ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState>
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

    fun onEvent(dashboardEvents: DashboardEvents){
        when (dashboardEvents){
            is DashboardEvents.VerifyToken -> {
                viewModelScope.launch {
                    kycRepository.validateToken(dashboardEvents.value).collect {
                        if(it.data?.state  == false){
                            _eventFlow.emit(UIEvent.Logout)
                        } else {
                            _state.value = it.data?.wallet?.let { it1 ->
                                state.value.copy(
                                    wallet = it1
                                )
                            }!!
                        }
                    }
                }
            }
            DashboardEvents.Cleanup -> {
                viewModelScope.launch {
                    userPreferences.setToken("")
                }

                viewModelScope.launch {
                    userPreferences.setEmail("")
                }
            }
        }
    }

    companion object {
        const val TAG = "DashboardViewModel"
    }

    sealed class UIEvent {
        object Logout: UIEvent()
    }
}

