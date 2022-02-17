package com.project.kycapp.data.repository

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.project.kycapp.data.api.KycApi
import com.project.kycapp.data.api.dto.KycRequestDto
import com.project.kycapp.data.api.dto.KycResponseMessageDto
import com.project.kycapp.models.*
import com.project.kycapp.repository.KycRepository
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class KycRepositoryImpl @Inject constructor(private val kycApi: KycApi): KycRepository {
    companion object{
        const val TAG = "KycRepositoryImpl"
    }
    override suspend fun register(account: Account): Flow<Resource<Authentication>> = flow {
        try{
            emit(Resource.Loading<Authentication>())

            val res = kycApi.registerUser(account)

            emit(Resource.Success<Authentication>(res.toAuthentication()))
        } catch (ex: Exception){
            Log.d(TAG, "register: ${ex.localizedMessage}")
            emit(Resource.Error<Authentication>(ex.localizedMessage, null))
        }
    }

    override suspend fun login(account: Account): Flow<Resource<Authentication>> = flow {
        try{
            emit(Resource.Loading<Authentication>())

            val res = kycApi.loginUser(account)

            emit(Resource.Success<Authentication>(res.toAuthentication()))
        } catch (ex: Exception){
            emit(Resource.Error<Authentication>(ex.localizedMessage))
        }
    }


    override suspend fun refreshToken(token: String): Flow<Resource<String>> = flow {
        kycApi.refreshToken(token)
    }

    override suspend fun validateToken(token: String): Flow<Resource<Validation>> = flow {
        try {
            emit(Resource.Loading<Validation>())

            val res = kycApi.validateToken(token)

            emit(Resource.Success<Validation>(res.toValidation()))
        } catch (ex: Exception){
            emit(Resource.Error<Validation>(ex.localizedMessage))
        }
    }

    override suspend fun browse(token: String): Flow<Resource<List<Kyc>>> = flow {
        try {
            emit(Resource.Loading<List<Kyc>>())

            val res = kycApi.browse(token)

            emit(Resource.Success<List<Kyc>>(res.data.map {
                it.record.toKyc()
            }))
        } catch (ex: Exception){
            emit(Resource.Error<List<Kyc>>(ex.localizedMessage))
        }
    }

    override suspend fun submit(token: String, requestDto: KycRequestDto): Flow<Resource<Message>> = flow {
        try {
            emit(Resource.Loading<Message>())

            val file = File(Uri.parse(requestDto.proofOfResidence).toFile().toURI())

            val file2 = File(Uri.parse(requestDto.proofOfId).toFile().toURI())

            Log.d(TAG, "submit: ${file.name} 2: ${file2.name}")
            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("firstname", requestDto.fname)
                .addFormDataPart("lastname", requestDto.lname)
                .addFormDataPart("dateOfBirth", requestDto.dob)
                .addFormDataPart("gender", requestDto.gender)
                .addFormDataPart("status", requestDto.status)
                .addFormDataPart("address", requestDto.address)
                .addFormDataPart("idNumber", requestDto.idNumber)
                .addFormDataPart("proofOfResidence", file.name, file
                    .asRequestBody("multipart/form-data".toMediaTypeOrNull()))
                .addFormDataPart("proofOfId", file2.name, file2
                    .asRequestBody("multipart/form-data".toMediaTypeOrNull()))
                .build()

            val res = kycApi.submit(token, multipartBody)

            emit(Resource.Success<Message>(res.toMessage()))
        } catch (ex: Exception){
            emit(Resource.Error<Message>(ex.localizedMessage))
        }
    }
}

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>() : Resource<T>()
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)
}