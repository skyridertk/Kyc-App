package com.project.kycapp.data.api

import com.project.kycapp.data.api.dto.LoginResponseDto
import com.project.kycapp.data.api.dto.RefreshTokenDto
import com.project.kycapp.data.api.dto.RegisterResponseDto
import com.project.kycapp.data.api.dto.ValidationDto
import com.project.kycapp.models.Account
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface KycApi {
    @POST("/api/login")
    suspend fun loginUser(account: Account): Response<LoginResponseDto>

    @POST("/api/register")
    suspend fun registerUser(@Body account: Account): RegisterResponseDto

    @GET("/api/refresh")
    suspend fun refreshToken(@Header("token") token: String): RefreshTokenDto

    @GET("/api/verify")
    suspend fun validateToken(@Header("token") token: String): ValidationDto

    @GET("/api/kyc")
    suspend fun getAllUserKyc(@Header("token") token: String): LoginResponseDto

    @POST("/api/kyc")
    suspend fun saveKyc(@Header("token") token: String, account: Account): LoginResponseDto
}