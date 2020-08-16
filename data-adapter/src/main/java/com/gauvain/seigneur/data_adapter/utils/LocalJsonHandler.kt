package com.gauvain.seigneur.data_adapter.utils

import android.app.Application
import com.gauvain.seigneur.data_adapter.model.RequestExceptionContent
import com.gauvain.seigneur.data_adapter.model.RequestResult
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

inline fun <reified T> readJson(
    application: Application,
    fileName: String
): RequestResult<T, RequestExceptionContent> {
    var result: RequestResult<T, RequestExceptionContent> = RequestResult.Error(
        RequestExceptionContent(
            RequestExceptionType.JSON_ERROR,
            "Unknown error while parsing local JSON"
        )
    )
    runCatching {
        parseJson(getJsonContent(application, fileName)) as T
    }

        .onFailure {
            result = RequestResult.Error(
                RequestExceptionContent(
                    RequestExceptionType.JSON_ERROR,
                    it.message
                )
            )
        }
        .onSuccess {
            result = RequestResult.Success(it)
        }
    return result
}


inline fun <reified T> parseJson(json: String): T {
    val type = object : TypeToken<T>() {}.type
    return GsonBuilder().create().fromJson(json, type)
}

fun getJsonContent(application: Application, fileName: String): String =
    application.assets.open(fileName).bufferedReader()
        .use {
            it.readText()
        }


