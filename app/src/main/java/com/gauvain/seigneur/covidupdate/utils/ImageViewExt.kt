package com.gauvain.seigneur.covidupdate.utils

import android.widget.ImageView
import coil.api.load
import coil.request.Request
import com.gauvain.seigneur.covidupdate.R

const val BASE_FLAG_URL = "https://hatscripts.github.io/circle-flags/flags/"
const val FLAG_IMG_FORMAT = ".svg"

fun ImageView.loadCountry(countryCode: String?, l: Request.Listener?=null) {
    this.load(
        "$BASE_FLAG_URL$countryCode$FLAG_IMG_FORMAT"
    ) {
        placeholder(R.drawable.ic_flag_place_holder)
        error(R.drawable.ic_flag_place_holder)
        fallback(R.drawable.ic_flag_place_holder)
        l?.let {
            listener(it)
        }

    }
}

