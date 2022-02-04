package com.project.kycapp.repository

import com.project.kycapp.models.Account

interface KycRepository {
    suspend fun register(account: Account)
    suspend fun login(account: Account)
    suspend fun refreshToken(token: String)
    suspend fun validateToken(token: String)
}