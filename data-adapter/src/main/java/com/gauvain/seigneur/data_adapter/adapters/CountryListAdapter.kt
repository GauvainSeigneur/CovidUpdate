package com.gauvain.seigneur.data_adapter.adapters

import android.app.Application
import com.gauvain.seigneur.domain.model.CountryItemModel
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.provider.CountryListProvider
import com.gauvain.seigneur.domain.provider.GetCountriesException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CountryListAdapter(private val application: Application) :
    CountryListProvider {

    companion object {
        const val COUNTRIES_ARRAY_NAME_KEY = "countries"
        const val COUNTRY_CODE_KEY = "abbreviation"
        const val COUNTRY_NAME_KEY = "country"
        const val COUNTRY_CODE_FILE_NAME = "country_code.json"
    }

    private var countryList = mutableListOf<CountryItemModel>()

    override fun getCountryList(): List<CountryItemModel> {
        return if (countryList.isEmpty()) {
            try {
                populateCountryList(getJsonContent(COUNTRY_CODE_FILE_NAME))
                countryList
            } catch (e: JSONException) {
                throw GetCountriesException(RequestExceptionType.JSON_ERROR, e.message)
            }
        } else {
            countryList
        }
    }

    private fun getJsonContent(fileName: String): String {
        return application.assets.open(fileName).bufferedReader()
            .use {
                it.readText()
            }
    }

    private fun populateCountryList(jsonFile: String) {
        val obj = JSONObject(jsonFile)
        val countries: JSONArray = obj.getJSONArray(COUNTRIES_ARRAY_NAME_KEY)
        for (i in 0 until countries.length()) {
            val jsonObject = countries.getJSONObject(i)
            val code = jsonObject.getString(COUNTRY_CODE_KEY)
            val name = jsonObject.getString(COUNTRY_NAME_KEY)
            countryList.add(CountryItemModel(name, code))
        }
    }
}