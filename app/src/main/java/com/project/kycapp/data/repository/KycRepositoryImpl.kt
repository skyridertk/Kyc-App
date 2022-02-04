package com.project.kycapp.data.repository

import com.project.kycapp.data.api.KycApi
import com.project.kycapp.models.Account
import com.project.kycapp.repository.KycRepository
import javax.inject.Inject

class KycRepositoryImpl @Inject constructor(private val kycApi: KycApi): KycRepository {
    override suspend fun register(account: Account) {
        kycApi.registerUser(account)
    }

    override suspend fun login(account: Account) {
        kycApi.loginUser(account)
    }


    override suspend fun refreshToken(token: String) {
        kycApi.refreshToken(token)
    }

    override suspend fun validateToken(token: String) {
        kycApi.validateToken(token)
    }
}

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)
}