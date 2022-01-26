package com.project.kycapp.repository

import com.project.kycapp.models.KyChainParticipant
import com.project.kycapp.models.LoginAuth

interface KycRepository {
    fun register(kyChainParticipant: KyChainParticipant)
    fun login(loginAuth: LoginAuth)
    fun updatePhysicalAddress()
    fun updateProofOfResidence()
}