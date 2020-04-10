package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.AllHistoryModel
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.provider.HistoryProvider

interface FetchAllHistoryUseCase {
    fun invoke(): Outcome<AllHistoryModel, ErrorType>

    companion object {
        fun create(provider: HistoryProvider): FetchAllHistoryUseCase =
            FetchAllHistoryUseCaseImpl(provider)
    }
}