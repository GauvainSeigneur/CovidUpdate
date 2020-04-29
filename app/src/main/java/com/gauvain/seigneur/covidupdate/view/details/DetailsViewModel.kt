package com.gauvain.seigneur.covidupdate.view.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.usecase.FetchCountryHistoryUseCase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailsViewModel(
    private val fetchCountryHistoryUseCase: FetchCountryHistoryUseCase,
    private val numberFormatProvider: NumberFormatProvider
) : ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        job.cancel()
    }


    val historyData: MutableLiveData<String> by lazy {
        fetchHistory()
        MutableLiveData<String>()
    }

    private fun fetchHistory() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                fetchCountryHistoryUseCase.invoke("france")
            }
            when (result) {
                is Outcome.Success -> {
                    historyData.value = result.data.country
                }
            }
        }

    }
}