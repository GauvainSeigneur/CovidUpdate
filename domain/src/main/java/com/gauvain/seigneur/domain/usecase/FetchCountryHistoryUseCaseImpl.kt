package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.provider.GetHistoryException
import com.gauvain.seigneur.domain.provider.HistoryProvider
import com.gauvain.seigneur.domain.utils.callProvider
import java.util.*

internal class FetchCountryHistoryUseCaseImpl(private val provider: HistoryProvider) :
    FetchCountryHistoryUseCase {

    override suspend fun invoke(countryName: String): Outcome<CountryHistoryModel, ErrorType>  =
        when (val result = callProvider ({ provider.history(countryName)}, GetHistoryException::class)) {
            is ProviderResult.Success -> {
                Outcome.Success(getCountryHistoryModel(result.data))
            }
            is ProviderResult.Error -> {
                Outcome.Error(result.error)
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

    /**
     * Api sends multiple history data for a day in a chronological order
     * Here we get only the first we found into this list, which is the last updated value for
     * the day selected
     */
    private fun getOneDayOnlyHistoryList(
        entries: List<StatisticsItemModel>
    ):
        List<CountryHistoryItemModel> {
        val dates = mutableSetOf<Date>()
        entries.map { dates.add(it.day) }

        val oneDayOnlyHistory = mutableListOf<CountryHistoryItemModel>()
        for (date in dates) {
            for (entry in entries) {
                if (entry.day == date) {
                    oneDayOnlyHistory.add(entry.toCountryHistoryItemModel())
                    break
                }
            }
        }
        return oneDayOnlyHistory
    }

}