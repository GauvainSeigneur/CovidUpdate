package com.gauvain.seigneur.data_adapter.mocks

import com.gauvain.seigneur.data_adapter.model.Statistics
import com.gauvain.seigneur.data_adapter.service.CovidService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.Calls
import retrofit2.mock.MockRetrofit
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException
import java.net.UnknownHostException

object CovidUpdateServiceMock {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://covid-193.p.rapidapi.com")
        .build()
    val behaviorDelegate: BehaviorDelegate<CovidService> =
        MockRetrofit.Builder(retrofit).build().create(CovidService::class.java)

    fun createServiceThatFail(t: Throwable) =
        object : CovidService {
            override fun statistics(country: String?): Call<Statistics> {
                return behaviorDelegate.returning(Calls.failure<Throwable>(t))
                    .statistics(country)
            }

            override fun history(country: String): Call<Statistics> {
                return behaviorDelegate.returning(Calls.failure<Throwable>(t))
                    .history(country)
            }

        }

    fun createServiceWithResponses(stats: Any? = null) =
        object : CovidService {
            override fun history(country: String): Call<Statistics> {
                return behaviorDelegate.returningResponse(stats).statistics(country)
            }

            override fun statistics(country: String?): Call<Statistics> {
                return behaviorDelegate.returningResponse(stats).statistics(country)
            }
        }
}