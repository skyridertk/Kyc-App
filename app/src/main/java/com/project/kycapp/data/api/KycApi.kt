package com.project.kycapp.data.api

import com.project.kycapp.models.Account

interface KycApi {
    suspend fun loginUser(account: Account)
    suspend fun registerUser(account: Account)
    suspend fun refreshToken(token: String)
    suspend fun validateToken(token: String)
}