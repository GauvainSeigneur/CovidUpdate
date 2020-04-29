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

    override fun invoke(): Outcome<AllHistoryModel, ErrorType> {
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

    private fun handleException(e: GetHistoryException): Outcome.Error<ErrorType> =
        when (e.type) {
            RequestExceptionType.UNKNOWN_HOST -> Outcome.Error(ErrorType.ERROR_UNKNOWN_HOST)
            RequestExceptionType.CONNECTION_LOST -> Outcome.Error(ErrorType.ERROR_CONNECTION_LOST)
            RequestExceptionType.UNAUTHORIZED -> Outcome.Error(ErrorType.ERROR_UNAUTHORIZED)
            RequestExceptionType.SERVER_INTERNAL_ERROR -> Outcome.Error(ErrorType.ERROR_INTERNAL_SERVER)
            else -> Outcome.Error(ErrorType.ERROR_UNKNOWN)
        }
}