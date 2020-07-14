package com.gauvain.seigneur.data_adapter.model

abstract class BaseResponse {
    //Type of request made
    abstract val get: String
    //errors that happened during the request
    abstract val errors: List<String>
    //in case of error like wrong api key
    abstract val message: String?
    //response received by server
    abstract val response: Any?
}
