package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.gauvain.seigneur.domain.provider.GetStatisticsException
import com.gauvain.seigneur.domain.provider.StatisticsProvider
import java.util.*

internal class FetchStatisticsUseCaseImpl(
    private val provider: StatisticsProvider) :
    FetchStatisticsUseCase {

    companion object {
        const val ALL_COUNTRY_NAME = "all"
        const val WORLD_COUNTRY_NAME = "world"
    }

    override fun invoke(country: String?): Outcome<List<StatisticsItemModel>, ErrorType> {
        return try {
            val result = provider.statistics(country)
            Outcome.Success(getListWithoutTotalOrWorldData(result))
        } catch (e: GetStatisticsException) {
            handleException(e)
        }
    }

    private fun getListWithoutTotalOrWorldData(statList: List<StatisticsItemModel>):
        List<StatisticsItemModel> {
        val listWithoutTotalOrWorldData = statList.toMutableList()
        for (item in statList) {
            if (item.country.toLowerCase(Locale.US) == WORLD_COUNTRY_NAME ||
                item.country.toLowerCase(Locale.US) == ALL_COUNTRY_NAME
            ) {
                listWithoutTotalOrWorldData.remove(item)
            }
        }
        return listWithoutTotalOrWorldData
    }

    private fun handleException(e: GetStatisticsException): Outcome.Error<ErrorType> =
        when (e.type) {
            RequestExceptionType.UNKNOWN_HOST -> Outcome.Error(ErrorType.ERROR_UNKNOWN_HOST)
            RequestExceptionType.CONNECTION_LOST-> Outcome.Error(ErrorType.ERROR_CONNECTION_LOST)
            RequestExceptionType.UNAUTHORIZED -> Outcome.Error(ErrorType.ERROR_UNAUTHORIZED)
            RequestExceptionType.SERVER_INTERNAL_ERROR -> Outcome.Error(ErrorType.ERROR_INTERNAL_SERVER)
            else -> Outcome.Error(ErrorType.ERROR_UNKNOWN)
        }
}