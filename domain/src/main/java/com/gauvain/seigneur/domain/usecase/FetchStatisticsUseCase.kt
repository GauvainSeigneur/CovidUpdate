package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.StatisticsModel
import com.gauvain.seigneur.domain.provider.StatisticsRepository

interface FetchStatisticsUseCase {
    suspend fun invoke(country: String?): Outcome<List<StatisticsModel>, ErrorType>

    companion object {
        fun create(repository: StatisticsRepository): FetchStatisticsUseCase =
            FetchStatisticsUseCaseImpl(repository)
    }
}