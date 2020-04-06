package com.gauvain.seigneur.covidupdate.view.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.*
import com.gauvain.seigneur.covidupdate.utils.RequestState
import com.gauvain.seigneur.covidupdate.utils.StringPresenter
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.StatisticsModel
import com.gauvain.seigneur.domain.provider.CountryCodeProvider
import com.gauvain.seigneur.domain.usecase.FetchStatisticsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

typealias StatisticsState = LiveDataState<StatisticsData>

class MainViewModel(
    private val fetchStatisticsUseCase: FetchStatisticsUseCase,
    private val countryCodeProvider: CountryCodeProvider
) :
    ViewModel() {

    val statisLiveItemData: MutableLiveData<StatisticsState> by lazy {
        fetchStatistics()
        MutableLiveData<StatisticsState>()
    }
    val loadingState: MutableLiveData<RequestState> = MutableLiveData()

    private fun fetchStatistics(country: String? = null) {
        viewModelScope.launch {
            loadingState.value = RequestState.IS_LOADING
            val result = withContext(Dispatchers.IO) {
                fetchStatisticsUseCase.invoke(country)
            }
            loadingState.value = RequestState.IS_LOADED
            when (result) {
                is Outcome.Success -> {
                    if (result.data.isEmpty()) {
                        statisLiveItemData.value = LiveDataState.Error(
                            ErrorData(
                                null,
                                StringPresenter(R.string.empty_list_title),
                                StringPresenter(R.string.empty_list_description)
                            )
                        )
                    } else {
                        val ascendingList = result.data.sortedByDescending { it.casesModel.total }
                        statisLiveItemData.value = LiveDataState.Success(
                            StatisticsData(
                                getTotalData(ascendingList),
                                getMostImpactCountries(ascendingList),
                                getListWithoutTotalOrWorldData(ascendingList).map {
                                    it.toStatisticsItemData(getCountryCode(it.country))
                                }
                            )
                        )
                    }
                }
                is Outcome.Error -> {
                    statisLiveItemData.value = setErrorLiveData(result.error)
                }
            }
        }
    }

    private fun getTotalData(statList: List<StatisticsModel>): TotalStatisticsData {
        var totalCase = 0
        var totalNewCases = 0
        for (item in statList) {
            if (item.country.toLowerCase(Locale.US) == "world") {
                totalCase = item.casesModel.total
                item.casesModel.new?.let {
                    totalNewCases += it.removePrefix("+").toInt()
                }
            }
        }
        return TotalStatisticsData(totalCase, totalNewCases)
    }

    private fun getMostImpactCountries(statList: List<StatisticsModel>):
        List<MostImpactCountriesData> {
        val mostImpactCountryList = mutableListOf<MostImpactCountriesData>()
        val totalCases = statList[0].casesModel.total
        val listWithoutTotalOrWorldData = getListWithoutTotalOrWorldData(statList)
        var totalFiveMostCountries = 0
        for (i in 0 until 5) {
            val item = MostImpactCountriesData(
                listWithoutTotalOrWorldData[i].country,
                listWithoutTotalOrWorldData[i].casesModel.total,
                listWithoutTotalOrWorldData[i].casesModel.total.toDouble() / totalCases
            )
            totalFiveMostCountries += listWithoutTotalOrWorldData[i].casesModel.total
            mostImpactCountryList.add(item)
        }

        //add other element
        mostImpactCountryList.add(
            MostImpactCountriesData(
            "Other",
            totalCases-totalFiveMostCountries,
                (totalCases-totalFiveMostCountries).toDouble() / totalCases))

        return mostImpactCountryList
    }

    private fun getListWithoutTotalOrWorldData(statList: List<StatisticsModel>):
        List<StatisticsModel> {
        val listWithoutTotalOrWorldData = statList.toMutableList()
        for (item in statList) {
            if (item.country.toLowerCase(Locale.US) == "world" || item.country.toLowerCase
                    (Locale.US) == "all"
            ) {
                listWithoutTotalOrWorldData.remove(item)
            }
        }
        return listWithoutTotalOrWorldData
    }

    private fun getCountryCode(countryName: String): String? =
        countryCodeProvider.getCountryCode(countryName)

    private fun setErrorLiveData(errorType: ErrorType): LiveDataState.Error =
        when (errorType) {
            else -> LiveDataState.Error(ErrorData())
        }
}