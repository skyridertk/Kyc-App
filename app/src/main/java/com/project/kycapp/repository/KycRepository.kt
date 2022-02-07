package com.project.kycapp.repository

import com.project.kycapp.data.repository.Resource
import com.project.kycapp.models.Account
import com.project.kycapp.models.Authentication
import com.project.kycapp.models.Validation
import kotlinx.coroutines.flow.Flow

interface KycRepository {
    suspend fun register(account: Account): Flow<Resource<Authentication>>
    suspend fun login(account: Account): Flow<Resource<String>>
    suspend fun refreshToken(token: String): Flow<Resource<String>>
    suspend fun validateToken(token: String): Flow<Resource<Validation>>
}