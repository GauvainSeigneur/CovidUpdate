package com.gauvain.seigneur.data_adapter.model

import com.google.gson.annotations.SerializedName

data class Regions(
    @SerializedName("continents")
    val regions: List<RegionItem>
)

data class RegionItem (
    val name: String
)

