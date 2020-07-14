package com.gauvain.seigneur.data_adapter.service

import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

class HeaderApiKeyInterceptor(val apiKeyValue: String) : Interceptor {

    companion object {
        const val API_HEADER_NAME = "x-rapidapi-key"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader(API_HEADER_NAME, apiKeyValue)
        return chain.proceed(builder.build())
    }
}