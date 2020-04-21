package com.gauvain.seigneur.data_adapter.mocks

import com.gauvain.seigneur.data_adapter.model.Statistics
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

object CountryJsonResponseMock {

    fun createCountryListResponse(): String =
        """
                {
                  "countries": [
                    {
                      "country": "Afghanistan",
                      "abbreviation": "AF"
                    },
                    {
                      "country": "Albania",
                      "abbreviation": "AL"
                    },
                    {
                      "country": "Algeria",
                      "abbreviation": "DZ"
                    },
                    {
                      "country": "American Samoa",
                      "abbreviation": "AS"
                    }
                  ] 
                }
                    """
}

