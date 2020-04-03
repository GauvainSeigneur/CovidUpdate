package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.Statistics
import com.gauvain.seigneur.domain.repository.StatisticsRepository

interface FetchStatisticsUseCase {

    fun invoke(country:String?): List<Statistics>

    companion object {
        fun create(repository: StatisticsRepository): FetchStatisticsUseCase =
            FetchStatisticsUseCaseImpl(repository)
    }
}

internal class FetchStatisticsUseCaseImpl(private val repository: StatisticsRepository) :
    FetchStatisticsUseCase {

    override fun invoke(country:String?): List<Statistics> {
        val result = runCatching {
            repository.statistics(country)
        }
        return result.getOrNull() ?: emptyList()
    }
}