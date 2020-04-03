package com.gauvain.seigneur.domain.repository

import com.gauvain.seigneur.domain.model.Statistics

interface StatisticsRepository {
    @Throws(GetStatisticsException::class)
    fun statistics(country:String?) : List<Statistics>

}

class GetStatisticsException : Exception()
