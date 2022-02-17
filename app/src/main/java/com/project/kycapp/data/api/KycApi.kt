package com.project.kycapp.data.api

import com.project.kycapp.data.api.dto.*
import com.project.kycapp.models.Account
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface KycApi {
    @POST("/api/login")
    suspend fun loginUser(@Body account: Account): AuthenticationResponseDto

    @POST("/api/register")
    suspend fun registerUser(@Body account: Account): AuthenticationResponseDto

    @GET("/api/refresh")
    suspend fun refreshToken(@Header("token") token: String): RefreshTokenDto

    @GET("/api/validate")
    suspend fun validateToken(@Header("token") token: String): ValidationDto

    @GET("/api/kyc")
    suspend fun browse(@Header("token") token: String): KycResponseDto2

    @POST("/api/kyc")
    suspend fun submit(@Header("token") token: String, @Body body: MultipartBody ): KycResponseMessageDto
}






















