package com.project.kycapp

import androidx.lifecycle.ViewModel
import com.project.kycapp.data.datastore.UserPreferences
import com.project.kycapp.repository.KycRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(userPreferences: UserPreferences, kycRepository: KycRepository) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState>
        get() = _state
}

data class MainState (
    val startNode: String = "login",
    val error: Boolean = false
)