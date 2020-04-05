package com.gauvain.seigneur.data_adapter.model

abstract class BaseResponse {
    abstract val get: String
    abstract val parameters: List<String>
    abstract val errors: List<String>
    //in case of error like api key uncorrect
    abstract val message: String?
}
