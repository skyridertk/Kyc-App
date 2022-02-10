package com.project.kycapp.data.api

import com.project.kycapp.data.api.dto.*
import com.project.kycapp.models.Account
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface KycApi {
    @POST("/api/login")
    suspend fun loginUser(account: Account): AuthenticationResponseDto

    @POST("/api/register")
    suspend fun registerUser(@Body account: Account): AuthenticationResponseDto

    @GET("/api/refresh")
    suspend fun refreshToken(@Header("token") token: String): RefreshTokenDto

    @GET("/api/verify")
    suspend fun validateToken(@Header("token") token: String): ValidationDto

    @GET("/api/kyc")
    suspend fun browse(@Header("token") token: String): KycResponseDto

    @POST("/api/kyc")
    suspend fun submit(@Header("token") token: String, @Body kycRequestDto: KycRequestDto): KycResponseMessageDto
}