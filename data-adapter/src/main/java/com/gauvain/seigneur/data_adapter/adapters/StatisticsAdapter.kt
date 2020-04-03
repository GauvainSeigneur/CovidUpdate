package com.gauvain.seigneur.data_adapter.adapters

import com.gauvain.seigneur.data_adapter.model.toDomainStatistics
import com.gauvain.seigneur.data_adapter.service.CovidService
import com.gauvain.seigneur.domain.repository.GetStatisticsException
import com.gauvain.seigneur.domain.repository.StatisticsRepository
import com.gauvain.seigneur.domain.model.Statistics

class StatisticsAdapter(val service: CovidService) :
    StatisticsRepository {

    override fun statistics(country: String?): List<Statistics> {
        val result = runCatching {
            service.statistics(country).execute()
        }
        return result.run {
            if (isFailure) {
                throw GetStatisticsException()
            }
            getOrNull()?.body().run {
                this?.stats?.map { stat ->
                    stat.toDomainStatistics()
                }
            } ?: throw GetStatisticsException()
        }
    }
}