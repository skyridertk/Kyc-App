package com.project.kycapp.data.repository

import android.util.Log
import com.project.kycapp.data.api.KycApi
import com.project.kycapp.data.api.dto.KycRequestDto
import com.project.kycapp.data.api.dto.KycResponseMessageDto
import com.project.kycapp.models.*
import com.project.kycapp.repository.KycRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KycRepositoryImpl @Inject constructor(private val kycApi: KycApi): KycRepository {
    companion object{
        const val TAG = "KycRepositoryImpl"
    }
    override suspend fun register(account: Account): Flow<Resource<Authentication>> = flow {
        try{
            val res = kycApi.registerUser(account)

            emit(Resource.Success<Authentication>(res.toAuthentication()))
        } catch (ex: Exception){
            emit(Resource.Error<Authentication>(ex.localizedMessage))
        }
    }

    override suspend fun login(account: Account): Flow<Resource<Authentication>> = flow {
        try{
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
            val res = kycApi.validateToken(token)

            emit(Resource.Success<Validation>(res.toValidation()))
        } catch (ex: Exception){
            emit(Resource.Error<Validation>(ex.localizedMessage))
        }
    }

    override suspend fun browse(token: String): Flow<Resource<List<Kyc>>> = flow {
        try {
            val res = kycApi.browse(token)

            emit(Resource.Success<List<Kyc>>(res.data.map { it.toKyc() }))
        } catch (ex: Exception){
            emit(Resource.Error<List<Kyc>>(ex.localizedMessage))
        }
    }

    override suspend fun submit(token: String, requestDto: KycRequestDto): Flow<Resource<Message>> = flow {
        try {
            val res = kycApi.submit(token, requestDto)

            emit(Resource.Success<Message>(res.toMessage()))
        } catch (ex: Exception){
            emit(Resource.Error<Message>(ex.localizedMessage))
        }
    }
}

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)
}