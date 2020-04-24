package com.gauvain.seigneur.data_adapter.adapters

import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import java.text.NumberFormat
import java.util.*

class NumberFormatAdapter :
    NumberFormatProvider {

    override fun format(number: Int): String {
        val locale = Locale(Locale.US.language)
        return NumberFormat.getInstance(locale).format(number)
    }

    override fun format(number: Double): String {
        val locale = Locale(Locale.US.language)
        return NumberFormat.getInstance(locale).format(number)
    }
}