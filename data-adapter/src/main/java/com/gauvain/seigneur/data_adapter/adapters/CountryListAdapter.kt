package com.gauvain.seigneur.data_adapter.adapters

import android.app.Application
import com.gauvain.seigneur.data_adapter.model.Countries
import com.gauvain.seigneur.data_adapter.model.RequestResult
import com.gauvain.seigneur.data_adapter.utils.readJson
import com.gauvain.seigneur.domain.model.CountryItemModel
import com.gauvain.seigneur.domain.provider.CountryListProvider
import com.gauvain.seigneur.domain.provider.GetCountriesException

class CountryListAdapter(private val application: Application) :
    CountryListProvider {

    companion object {
        const val COUNTRY_CODE_FILE_NAME = "country_code.json"
    }

    override fun getCountryList(): List<CountryItemModel> =
        when (val result = readJson<Countries>(application, COUNTRY_CODE_FILE_NAME)) {
            is RequestResult.Success -> {
                result.data.countries.map { item -> CountryItemModel(
                    country = item.country,
                    code = item.abbreviation
                 ) }
            }
            is RequestResult.Error -> {
                throw GetCountriesException(result.error.exceptionType, result.error.message)
            }
        }
}