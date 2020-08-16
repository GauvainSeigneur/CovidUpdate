package com.gauvain.seigneur.data_adapter.model

import com.google.gson.annotations.SerializedName

data class Countries(
    @SerializedName("countries")
    val countries: List<CountryItem>
)

data class CountryItem (
    val country: String,
    val abbreviation: String
)

