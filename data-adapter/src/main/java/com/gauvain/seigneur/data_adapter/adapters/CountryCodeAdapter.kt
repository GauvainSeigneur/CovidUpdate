package com.gauvain.seigneur.data_adapter.adapters

import android.app.Application
import com.gauvain.seigneur.data_adapter.model.Country
import com.gauvain.seigneur.domain.provider.CountryCodeProvider
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class CountryCodeAdapter(application: Application) :
    CountryCodeProvider {

    private val jsonFile: String = application.assets.open("country_code.json").bufferedReader()
        .use {
            it.readText()
        }
    private var countryList = arrayListOf<Country>()

    override fun getCountryCode(country: String): String? {
        if (countryList.isEmpty()) {
            try {
                populateCountryList()
            } catch (e: JSONException) {
                return null
            }
        }
        return fetchCountryCode(country)
    }

    private fun fetchCountryCode(countryName:String):String? {
        var code :String?=null
        for (item in countryList) {
            if(countryName.toLowerCase(Locale.ENGLISH) == item.country.toLowerCase(Locale.ENGLISH)) {
                code =  item.code.toLowerCase(Locale.ENGLISH)
            }
        }
        return code
    }

    private fun populateCountryList() {
        val obj = JSONObject(jsonFile)
        val countries: JSONArray = obj.getJSONArray("countries")
        for (i in 0 until countries.length()) {
            val jsonObject = countries.getJSONObject(i)
            val code = jsonObject.getString("abbreviation")
            val name = jsonObject.getString("country")
            countryList.add(Country(name, code))
        }
    }

}