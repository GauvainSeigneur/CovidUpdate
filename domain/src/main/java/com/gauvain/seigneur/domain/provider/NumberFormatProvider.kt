package com.gauvain.seigneur.domain.provider

interface NumberFormatProvider {
    fun format(number: Int): String
    fun format(number: Double): String
    fun formatToPercentage(number: Double): String
}

