package com.project.kycapp.views.detail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
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
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState>
        get() = _state

    init {

        savedStateHandle.get<String>("data")?.let { userData ->
            Log.d(TAG, "${userData}: ")

            val decoded = URLDecoder.decode(userData, StandardCharsets.UTF_8.toString());

            viewModelScope.launch {
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                val jsonAdapter = moshi.adapter(Kyc::class.java).lenient()
                val userObject = jsonAdapter.fromJson(decoded)

                userObject?.let {
                    _state.value = state.value.copy(
                        idNumber = it.idNumber,
                        firstName = it.firstName,
                        surname = it.surname,
                        address = it.address,
                        phoneNumber = it.phoneNumber,
                        gender = it.gender,
                        status= it.status,
                        dateOfBirth = it.dateOfBirth,
                        approvalCount = it.approvalCount,
                        owner = it.owner,
                        proofOfId = it.proofOfId,
                        proofOfResidence = it.proofOfResidence,
                        assetID = it.id
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

fun convertBase64ToBitmap(base64String: String): Bitmap? {
    val imageBytes = Base64.decode(base64String.split(",")[1], Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    return decodedImage
}