package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.provider.GetHistoryException
import com.gauvain.seigneur.domain.provider.HistoryProvider
import java.util.*

internal class FetchAllHistoryUseCaseImpl(private val provider: HistoryProvider) :
    FetchAllHistoryUseCase {

    companion object {
        const val ALL_COUNTRY = "all"
    }

    override suspend fun invoke(): Outcome<AllHistoryModel, ErrorType> {
        return try {
            val result = provider.history(ALL_COUNTRY)
            Outcome.Success(getAllHistoryModel(result))
        } catch (e: GetHistoryException) {
            handleException(e)
        }
    }

    private fun getAllHistoryModel(result: List<StatisticsItemModel>): AllHistoryModel {
        return result.run {
            val historyList = this.map {
                it.toAllHistoryItemModel()
            }
            AllHistoryModel(
                totalCases = this[0].casesModel.total,
                totalNewCases = this[0].casesModel.new,
                history = getSmallHistoryList(historyList)
            )
        }
    }

    private fun getSmallHistoryList(entry: List<AllHistoryItemModel>): List<AllHistoryItemModel> {
        val dates = mutableSetOf<Date>()
        val smallHistoryList = mutableListOf<AllHistoryItemModel>()
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

    private fun handleException(e: GetHistoryException): Outcome.Error<ErrorType> =
        when (e.type) {
            RequestExceptionType.UNKNOWN_HOST -> Outcome.Error(ErrorType.ERROR_UNKNOWN_HOST)
            RequestExceptionType.CONNECTION_LOST -> Outcome.Error(ErrorType.ERROR_CONNECTION_LOST)
            RequestExceptionType.UNAUTHORIZED -> Outcome.Error(ErrorType.ERROR_UNAUTHORIZED)
            RequestExceptionType.SERVER_INTERNAL_ERROR -> Outcome.Error(ErrorType.SERVER_INTERNAL_ERROR)
            else -> Outcome.Error(ErrorType.ERROR_UNKNOWN)
        }
}