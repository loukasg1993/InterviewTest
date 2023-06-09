package com.betsson.interviewteest.api

import retrofit2.http.GET
import com.betsson.interviewteest.data.Bet

interface ApiService {
    @GET("someEndpoint")
    suspend fun getBets(): List<Bet>

}