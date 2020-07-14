package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.provider.GetHistoryException
import com.gauvain.seigneur.domain.provider.HistoryProvider
import com.gauvain.seigneur.domain.utils.callProvider
import java.util.*

internal class FetchAllHistoryUseCaseImpl(private val provider: HistoryProvider) :
    FetchAllHistoryUseCase {

    companion object {
        const val ALL_COUNTRY = "all"
    }

    override fun invoke(): Outcome<AllHistoryModel, ErrorType> =
        when (
            val result = callProvider(
                { provider.history(ALL_COUNTRY) },
                GetHistoryException::class
            )
            ) {
            is ProviderResult.Success -> {
                Outcome.Success(getAllHistoryModel(result.data))
            }
            is ProviderResult.Error -> {
                Outcome.Error(result.error)
            }
        }

    private fun getAllHistoryModel(result: List<StatisticsItemModel>): AllHistoryModel {
        return result.run {
            val historyList = this.map {
                it.toAllActiveHistoryItemModel()
            }
            AllHistoryModel(
                totalCases = this[0].casesModel.total,
                totalActiveCases = this[0].casesModel.active,
                totalNewCases = this[0].casesModel.new,
                activeHistory = getSmallHistoryList(historyList)
            )
        }
    }

    private fun getSmallHistoryList(entry: List<AllActiveHistoryItemModel>): List<AllActiveHistoryItemModel> {
        val dates = mutableSetOf<Date>()
        val smallHistoryList = mutableListOf<AllActiveHistoryItemModel>()
        entry.map {
            dates.add(it.day)
        }
        for (d in dates) {
            for (i in entry) {
                if (i.day == d) {
                    smallHistoryList.add(i)
                    break
                }
            }
        }
        return smallHistoryList
    }
}