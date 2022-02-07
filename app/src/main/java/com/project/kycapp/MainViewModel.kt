package com.project.kycapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.kycapp.data.datastore.UserPreferences
import com.project.kycapp.repository.KycRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(userPreferences: UserPreferences, kycRepository: KycRepository) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState>
        get() = _state

    init {
        viewModelScope.launch {
            userPreferences.tokenFlow().collect {
                if(it.isNotEmpty()){
                    _state.value = state.value.copy(
                        startNode = "main"
                    )
                } else {
                    _state.value = state.value.copy(
                        startNode = "authentication"
                    )
                }

                _state.value = state.value.copy(
                    isLoading = false
                )
            }
        }
    }
}

data class MainState (
    val startNode: String = "authentication",
    val isLoading: Boolean = true
)