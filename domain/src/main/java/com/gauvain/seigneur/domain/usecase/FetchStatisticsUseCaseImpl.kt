package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.ProviderResult
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.gauvain.seigneur.domain.provider.GetStatisticsException
import com.gauvain.seigneur.domain.provider.StatisticsProvider
import com.gauvain.seigneur.domain.utils.callProvider
import java.util.*

internal class FetchStatisticsUseCaseImpl(
    private val provider: StatisticsProvider
) : FetchStatisticsUseCase {

    companion object {
        const val ALL_COUNTRY_NAME = "all"
        const val WORLD_COUNTRY_NAME = "world"
        const val EUROPE = "europe"
        const val NORTH_AMERICA = "north-america"
        const val SOUTH_AMERICA = "south-america"
        const val ASIA = "asia"
        const val AFRICA = "africa"
    }

    override fun invoke(country: String?): Outcome<List<StatisticsItemModel>, ErrorType> =
        when (
            val result = callProvider(
                { provider.statistics(country) },
                GetStatisticsException::class
            )) {
            is ProviderResult.Success -> {
                Outcome.Success(getListWithoutAllWorldOrContinentData(result.data))
            }
            is ProviderResult.Error -> {
                Outcome.Error(result.error)
            }
        }

    private fun getListWithoutAllWorldOrContinentData(statList: List<StatisticsItemModel>):
        List<StatisticsItemModel> {
        val excludedRegions = listOf(
            ALL_COUNTRY_NAME,
            WORLD_COUNTRY_NAME,
            EUROPE,
            NORTH_AMERICA,
            SOUTH_AMERICA,
            ASIA,
            AFRICA
        )
        return statList.filterNot { it.country.toLowerCase(Locale.US) in excludedRegions }
    }
}