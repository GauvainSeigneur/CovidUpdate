package com.gauvain.seigneur.data_adapter.model

import com.gauvain.seigneur.domain.model.RequestExceptionType

data class RequestExceptionContent(
    val exceptionType: RequestExceptionType,
    val message: String? = null
)
