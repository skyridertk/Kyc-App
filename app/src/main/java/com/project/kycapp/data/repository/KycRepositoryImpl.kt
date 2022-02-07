package com.project.kycapp.data.repository

import android.util.Log
import com.project.kycapp.data.api.KycApi
import com.project.kycapp.models.Account
import com.project.kycapp.models.Authentication
import com.project.kycapp.models.Validation
import com.project.kycapp.repository.KycRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KycRepositoryImpl @Inject constructor(private val kycApi: KycApi): KycRepository {
    companion object{
        const val TAG = "KycRepositoryImpl"
    }
    override suspend fun register(account: Account): Flow<Resource<Authentication>> = flow {
        val res = kycApi.registerUser(account)

        emit(Resource.Success<Authentication>(res.toAuthentication()))
    }

    override suspend fun login(account: Account): Flow<Resource<String>> = flow {
        val res = kycApi.loginUser(account)

        if (res.isSuccessful){
            emit(Resource.Success<String>("Complete"))
        } else {
            emit(Resource.Error<String>("Failed"))
        }
    }


    override suspend fun refreshToken(token: String): Flow<Resource<String>> = flow {
        kycApi.refreshToken(token)
    }

    override suspend fun validateToken(token: String): Flow<Resource<Validation>> = flow {
        val res = kycApi.validateToken(token)

        emit(Resource.Success<Validation>(res.toValidation()))
    }
}

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)
}