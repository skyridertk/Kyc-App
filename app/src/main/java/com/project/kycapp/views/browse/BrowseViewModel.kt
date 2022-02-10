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
class BrowseViewModel @Inject constructor(userPreferences: UserPreferences, val kycRepository: KycRepository) : ViewModel() {
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

    fun onEvent(browseEvents: BrowseEvents){
        when (browseEvents){
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
                Log.d(TAG, "INSIDE: ")
                when (resource){
                    is Resource.Error -> TODO()
                    is Resource.Success -> {
                        _state.value = resource.data?.let {
                            Log.d(TAG, "DATA: ${it}")
                            state.value.copy(
                                listOfKyc = it,
                                isLoading = false
                            )
                        }!!
                    }
                }
            }
        } catch (ex: Exception){
            Log.d(TAG, "getKyc: Exception: ${ex.localizedMessage}")
        }
    }
    
    companion object {
        const val TAG = "BrowseViewModel"
    }

    sealed class UIEvent {
        data class OpenDetail(val kyc: Kyc) : UIEvent()
    }
}