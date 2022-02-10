package com.project.kycapp.repository

import com.project.kycapp.data.api.dto.KycRequestDto
import com.project.kycapp.data.api.dto.KycResponseMessageDto
import com.project.kycapp.data.repository.Resource
import com.project.kycapp.models.*
import kotlinx.coroutines.flow.Flow

interface KycRepository {
    suspend fun register(account: Account): Flow<Resource<Authentication>>
    suspend fun login(account: Account): Flow<Resource<Authentication>>
    suspend fun refreshToken(token: String): Flow<Resource<String>>
    suspend fun validateToken(token: String): Flow<Resource<Validation>>
    suspend fun browse(token: String): Flow<Resource<List<Kyc>>>
    suspend fun submit(token: String, requestDto: KycRequestDto): Flow<Resource<Message>>
}