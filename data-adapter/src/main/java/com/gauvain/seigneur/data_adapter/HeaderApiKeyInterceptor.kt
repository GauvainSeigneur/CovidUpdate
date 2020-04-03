package com.gauvain.seigneur.data_adapter

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response

class HeaderApiKeyInterceptor(val apiKeyValue: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("x-rapidapi-key", apiKeyValue)
        return chain.proceed(builder.build())
    }
}