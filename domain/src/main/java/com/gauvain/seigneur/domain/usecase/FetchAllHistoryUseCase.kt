package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.AllHistoryModel
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.provider.HistoryProvider

interface FetchAllHistoryUseCase {
    suspend fun invoke(): Outcome<AllHistoryModel, ErrorType>

    companion object {
        fun create(provioder: HistoryProvider): FetchAllHistoryUseCase =
            FetchAllHistoryUseCaseImpl(provioder)
    }
}