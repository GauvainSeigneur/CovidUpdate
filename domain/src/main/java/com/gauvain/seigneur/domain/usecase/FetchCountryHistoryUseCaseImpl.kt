package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.provider.GetHistoryException
import com.gauvain.seigneur.domain.provider.HistoryProvider
import java.util.*

internal class FetchCountryHistoryUseCaseImpl(private val provider: HistoryProvider) :
    FetchCountryHistoryUseCase {

    override suspend fun invoke(countryName: String): Outcome<CountryHistoryModel, ErrorType> {
        return try {
            val result = provider.history(countryName)
            Outcome.Success(getCountryHistoryModel(result))
        } catch (e: GetHistoryException) {
            handleException(e)
        }
    }

    private fun getCountryHistoryModel(result: List<StatisticsItemModel>): CountryHistoryModel {
        return result.run {
            CountryHistoryModel(
                country = this[0].country,
                history = getOneDayOnlyHistoryList(result)
            )
        }
    }

    private fun getOneDayOnlyHistoryList(
        entry: List<StatisticsItemModel>
    ):
        List<CountryHistoryItemModel> {
        val dates = mutableSetOf<Date>()
        entry.map { dates.add(it.day) }
        val oneDayOnlyHistory = mutableListOf<CountryHistoryItemModel>()
        for (d in dates) {
            for (e in entry) {
                if (e.day == d) {
                    oneDayOnlyHistory.add(
                        e.toCountryHistoryItemModel()
                    )
                    break
                }
            }
        }

        return oneDayOnlyHistory
    }

    private fun handleException(e: GetHistoryException): Outcome.Error<ErrorType> =
        when (e.type) {
            RequestExceptionType.UNKNOWN_HOST -> Outcome.Error(ErrorType.ERROR_UNKNOWN_HOST)
            RequestExceptionType.CONNECTION_LOST -> Outcome.Error(ErrorType.ERROR_CONNECTION_LOST)
            RequestExceptionType.UNAUTHORIZED -> Outcome.Error(ErrorType.ERROR_UNAUTHORIZED)
            RequestExceptionType.SERVER_INTERNAL_ERROR -> Outcome.Error(ErrorType.ERROR_INTERNAL_SERVER)
            else -> Outcome.Error(ErrorType.ERROR_UNKNOWN)
        }
}