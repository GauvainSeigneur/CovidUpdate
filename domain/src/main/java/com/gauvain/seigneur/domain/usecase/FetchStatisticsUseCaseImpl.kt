package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.request.Outcome
import com.gauvain.seigneur.domain.model.StatisticsModel
import com.gauvain.seigneur.domain.provider.GetStatisticsException
import com.gauvain.seigneur.domain.provider.StatisticsRepository

internal class FetchStatisticsUseCaseImpl(private val repository: StatisticsRepository) :
    FetchStatisticsUseCase {

    override suspend fun invoke(country: String?): Outcome<List<StatisticsModel>,
        GetStatisticsException> {
        return try {
            val result = repository.statistics(country)
            Outcome.Success(result)
        } catch (e: GetStatisticsException) {
            Outcome.Error(e)
        }
    }
}