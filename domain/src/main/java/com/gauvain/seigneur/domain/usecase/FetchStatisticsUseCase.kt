package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.request.Outcome
import com.gauvain.seigneur.domain.model.StatisticsModel
import com.gauvain.seigneur.domain.provider.GetStatisticsException
import com.gauvain.seigneur.domain.provider.StatisticsRepository

interface FetchStatisticsUseCase {
    suspend fun invoke(country: String?): Outcome<List<StatisticsModel>, GetStatisticsException>

    companion object {
        fun create(repository: StatisticsRepository): FetchStatisticsUseCase =
            FetchStatisticsUseCaseImpl(repository)
    }
}