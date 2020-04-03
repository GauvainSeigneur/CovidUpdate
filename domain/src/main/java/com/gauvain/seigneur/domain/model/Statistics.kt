package com.gauvain.seigneur.domain.model

data class Statistics(
    val country: String,
    val cases: Cases,
    val deaths: Deaths,
    val day: String,
    val time: String
)

data class Cases(
    val new: String? = null,
    val active: Int,
    val critical: Int,
    val recovered: Int,
    val total: Int
)

data class Deaths(
    val new: String? = null,
    val total: Int
)