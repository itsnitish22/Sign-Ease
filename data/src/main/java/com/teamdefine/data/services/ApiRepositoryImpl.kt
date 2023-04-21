package com.teamdefine.data.services

import com.teamdefine.domain.repository.ApiRepository
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    val apiService: ApiService
) : ApiRepository {
    override fun getName(): String {
        return "Nitish"
    }
}