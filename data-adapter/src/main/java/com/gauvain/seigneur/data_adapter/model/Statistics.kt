package com.gauvain.seigneur.data_adapter.model

import com.google.gson.annotations.SerializedName

data class Statistics(
    @SerializedName("get")
    val get: String,
    @SerializedName("parameters")
    val parameters: List<String>,
    @SerializedName("errors")
    val errors: List<String>,
    @SerializedName("response")
    val stats: List<Stat>,
    //in case of error like api key uncorrect
    @SerializedName("message") val message: String?=null
)

data class Stat(
    @SerializedName("country")
    val country: String,
    val cases: Cases,
    val deaths: Deaths,
    val day: String,
    val time: String
)

data class Cases(
    @SerializedName("new")
    val new: String? = null,
    @SerializedName("active")
    val active: Int,
    @SerializedName("critical")
    val critical: Int,
    @SerializedName("recovered")
    val recovered: Int,
    @SerializedName("total")
    val total: Int
)

data class Deaths(
    @SerializedName("new")
    val new: String? = null,
    @SerializedName("total")
    val total: Int
)

