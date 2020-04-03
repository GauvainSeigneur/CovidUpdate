package com.gauvain.seigneur.data_adapter.service

import com.gauvain.seigneur.data_adapter.model.Statistics
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface CovidService {

    @GET("/statistics")
    fun statistics(
        @Query("country")
        country: String?=null
    ): Call<Statistics>
}