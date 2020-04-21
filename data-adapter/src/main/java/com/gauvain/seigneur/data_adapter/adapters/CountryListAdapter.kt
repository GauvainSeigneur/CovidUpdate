package com.gauvain.seigneur.data_adapter.adapters

import android.app.Application
import com.gauvain.seigneur.domain.model.CountryItemModel
import com.gauvain.seigneur.domain.provider.CountryListProvider
import com.gauvain.seigneur.domain.provider.GetCountriesException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CountryListAdapter(private val application: Application) :
    CountryListProvider {

    private var countryList = arrayListOf<CountryItemModel>()

    override fun getCountryList(): List<CountryItemModel> {
        if (countryList.isEmpty()) {
            try {
                val jsonFile: String = application.assets.open("country_code.json").bufferedReader()
                    .use {
                        it.readText()
                    }
                populateCountryList(jsonFile)
                return countryList
            } catch (e: JSONException) {
                throw GetCountriesException()
            }
        } else {
            return countryList
        }
    }

    private fun populateCountryList(jsonFile: String) {
        val obj = JSONObject(jsonFile)
        val countries: JSONArray = obj.getJSONArray("countries")
        for (i in 0 until countries.length()) {
            val jsonObject = countries.getJSONObject(i)
            val code = jsonObject.getString("abbreviation")
            val name = jsonObject.getString("country")
            countryList.add(CountryItemModel(name, code))
        }
    }
}