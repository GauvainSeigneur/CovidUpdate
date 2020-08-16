package com.gauvain.seigneur.data_adapter.adapters

import android.app.Application
import com.gauvain.seigneur.data_adapter.model.Regions
import com.gauvain.seigneur.data_adapter.model.RequestResult
import com.gauvain.seigneur.data_adapter.utils.readJson
import com.gauvain.seigneur.domain.model.RegionItemModel
import com.gauvain.seigneur.domain.provider.GetRegionsException
import com.gauvain.seigneur.domain.provider.RegionListProvider

class RegionListAdapter(private val application: Application) :
    RegionListProvider {

    companion object {
        const val REGION_FILE_NAME = "region.json"
    }

    override fun getRegionList(): List<RegionItemModel> =
        when (val result = readJson<Regions>(application, REGION_FILE_NAME)) {
            is RequestResult.Success -> {
                result.data.regions.map { item ->
                    RegionItemModel(
                        name = item.name
                    )
                }
            }
            is RequestResult.Error -> {
                throw GetRegionsException(result.error.exceptionType, result.error.message)
            }
        }

}