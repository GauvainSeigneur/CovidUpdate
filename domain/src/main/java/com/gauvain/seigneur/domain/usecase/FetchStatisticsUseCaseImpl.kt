package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.gauvain.seigneur.domain.provider.GetStatisticsException
import com.gauvain.seigneur.domain.provider.StatisticsProvider
import java.util.*

internal class FetchStatisticsUseCaseImpl(
    private val provider: StatisticsProvider
) :
    FetchStatisticsUseCase {

    companion object {
        const val ALL_COUNTRY_NAME = "all"
        const val WORLD_COUNTRY_NAME = "world"
        const val EUROPE = "europe"
        const val NORTH_AMERICA = "north-america"
        const val SOUTH_AMERICA = "south-america"
        const val ASIA = "asia"
        const val AFRICA = "africa"
    }

    override fun invoke(country: String?): Outcome<List<StatisticsItemModel>, ErrorType> {
        return try {
            val result = provider.statistics(country)
            Outcome.Success(getListWithoutAllWorldOrContinentData(result))
        } catch (e: GetStatisticsException) {
            handleException(e)
        }
    }

    private fun getListWithoutAllWorldOrContinentData(statList: List<StatisticsItemModel>):
        List<StatisticsItemModel> {
        val listWithoutTotalOrWorldData = statList.toMutableList()
        for (item in statList) {
            val country = item.country.toLowerCase(Locale.US)
            if (country == WORLD_COUNTRY_NAME || country == ALL_COUNTRY_NAME ||
                country == EUROPE || country == NORTH_AMERICA ||
                country == ASIA || country == SOUTH_AMERICA ||
                country == AFRICA
            ) {
                listWithoutTotalOrWorldData.remove(item)
            }
        }
        return listWithoutTotalOrWorldData
    }

    private fun handleException(e: GetStatisticsException): Outcome.Error<ErrorType> =
        when (e.type) {
            RequestExceptionType.UNKNOWN_HOST -> Outcome.Error(ErrorType.ERROR_UNKNOWN_HOST)
            RequestExceptionType.CONNECTION_LOST -> Outcome.Error(ErrorType.ERROR_CONNECTION_LOST)
            RequestExceptionType.UNAUTHORIZED -> Outcome.Error(ErrorType.ERROR_UNAUTHORIZED)
            RequestExceptionType.SERVER_INTERNAL_ERROR -> Outcome.Error(ErrorType.ERROR_INTERNAL_SERVER)
            else -> Outcome.Error(ErrorType.ERROR_UNKNOWN)
        }
}