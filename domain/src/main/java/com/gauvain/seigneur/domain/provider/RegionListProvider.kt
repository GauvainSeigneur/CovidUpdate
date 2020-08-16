package com.gauvain.seigneur.domain.provider

import com.gauvain.seigneur.domain.model.BaseProviderException
import com.gauvain.seigneur.domain.model.RegionItemModel
import com.gauvain.seigneur.domain.model.RequestExceptionType

interface RegionListProvider {
    @Throws(GetRegionsException::class)
    fun getRegionList(): List<RegionItemModel>
}

class GetRegionsException(type: RequestExceptionType,
                            description: String? = null) : BaseProviderException(type, description)


