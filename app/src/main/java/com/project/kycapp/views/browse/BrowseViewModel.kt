package com.project.kycapp.views.browse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.kycapp.data.datastore.UserPreferences
import com.project.kycapp.data.repository.Resource
import com.project.kycapp.models.Kyc
import com.project.kycapp.repository.KycRepository
import com.project.kycapp.views.login.LoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor(
    userPreferences: UserPreferences,
    val kycRepository: KycRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BrowseState())
    val state: StateFlow<BrowseState>
        get() = _state

    private val _token = MutableStateFlow("")

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            userPreferences.tokenFlow().collect { token ->
                _token.value = token
            }
        }
    }

    fun onEvent(browseEvents: BrowseEvents) {
        when (browseEvents) {
            BrowseEvents.FETCH -> {
                getKyc(_token.value)
            }
            is BrowseEvents.OpenKyc -> {
                viewModelScope.launch {
                    _eventFlow.emit(UIEvent.OpenDetail(browseEvents.kyc))
                }
            }
        }
    }

    private fun getKyc(token: String) = viewModelScope.launch {
        try {
            kycRepository.browse(token).collect { resource ->

                when (resource) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            isLoading = true,
                            error = true
                        )

                        _eventFlow.emit(UIEvent.Error("Error Occured"))
                    }
                    is Resource.Success -> {
                        val listKyc = resource.data ?: emptyList()

                        _state.value = state.value.copy(
                            listOfKyc = listKyc,
                            isLoading = false,
                            error = false
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true,
                            error = false
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            _eventFlow.emit(UIEvent.Error(ex.localizedMessage ?: ""))
        }
    }

    companion object {
        const val TAG = "BrowseViewModel"
    }

    sealed class UIEvent {
        data class OpenDetail(val kyc: Kyc) : UIEvent()
        data class Error(val message: String) : UIEvent()
    }
}