package com.betsson.interviewteest.api

import com.betsson.interviewteest.data.Bet

class Repository(private val apiService: ApiService) {
    suspend fun getBets() : List<Bet> {
        return apiService.getBets()
    }

}