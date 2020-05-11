package com.gauvain.seigneur.data_adapter.adapters

import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import java.text.DecimalFormat
import java.text.NumberFormat

class NumberFormatAdapter : NumberFormatProvider {

    override fun format(number: Int): String {
        return NumberFormat.getInstance().format(number)
    }

    override fun format(number: Double): String {
        return NumberFormat.getInstance().format(number)
    }

    override fun formatToPercentage(number: Double): String {
        val formatter = DecimalFormat("00.##")
        return formatter.format(number)
    }
}