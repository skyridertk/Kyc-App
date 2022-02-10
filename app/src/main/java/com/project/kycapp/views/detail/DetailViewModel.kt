package com.project.kycapp.views.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.kycapp.models.Kyc
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState>
        get() = _state

    init {
        savedStateHandle.get<String>("data")?.let {

            viewModelScope.launch {
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                val jsonAdapter = moshi.adapter(Kyc::class.java).lenient()
                val userObject = jsonAdapter.fromJson(it)

                userObject?.let {
                    _state.value = state.value.copy(
                        idNumber = it.idNumber,
                        firstName = it.firstName,
                        surname = it.surname,
                        address = it.address,
                        phoneNumber = it.phoneNumber,
                        gender = it.gender,
                        dateOfBirth = it.dateOfBirth,
                        approvalCount = it.approvalCount,
                        owner = it.owner
                    )
                }

                Log.d(TAG, "${userObject}: ")
            }
        }
    }

    companion object {
        const val TAG = "DetailViewModel"
    }
}