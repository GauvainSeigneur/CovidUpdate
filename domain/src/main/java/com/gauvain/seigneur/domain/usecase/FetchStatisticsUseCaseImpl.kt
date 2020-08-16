package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.provider.GetRegionsException
import com.gauvain.seigneur.domain.provider.GetStatisticsException
import com.gauvain.seigneur.domain.provider.RegionListProvider
import com.gauvain.seigneur.domain.provider.StatisticsProvider
import com.gauvain.seigneur.domain.utils.callProvider
import java.util.*

internal class FetchStatisticsUseCaseImpl(
    private val provider: StatisticsProvider,
    private val regionProvider : RegionListProvider
) : FetchStatisticsUseCase {

    override fun invoke(country: String?): Outcome<List<StatisticsItemModel>, ErrorType> =
        getData(country)

    private fun getData(country: String?): Outcome<List<StatisticsItemModel>, ErrorType> {
        return when(val regions = getRegions()) {
            is Outcome.Success -> {
                //perform call to have all statistics
                when (
                    val result = callProvider(
                        { provider.statistics(country) },
                        GetStatisticsException::class
                    )) {
                    is ProviderResult.Success -> {
                        Outcome.Success(
                            getListWithoutAllWorldOrContinentData(
                                regions.data.map { item -> item.name },
                                result.data))
                    }
                    is ProviderResult.Error -> {
                        Outcome.Error(result.error)
                    }
                }
            }
            is Outcome.Error -> {
                Outcome.Error(regions.error)
            }
        }
    }

    private fun getRegions(): Outcome<List<RegionItemModel>, ErrorType>  =
        when (
            val result = callProvider(
                { regionProvider.getRegionList() },
                GetRegionsException::class
            )) {
            is ProviderResult.Success -> {
                Outcome.Success(result.data)
            }
            is ProviderResult.Error -> {
                Outcome.Error(result.error)
            }
        }


    private fun getListWithoutAllWorldOrContinentData(
        excludedRegions : List<String>,
        statList: List<StatisticsItemModel>):
        List<StatisticsItemModel> {
        return statList.filterNot { it.country.toLowerCase(Locale.US) in excludedRegions }
    }
}