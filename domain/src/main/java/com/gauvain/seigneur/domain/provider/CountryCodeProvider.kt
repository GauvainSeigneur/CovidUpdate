package com.gauvain.seigneur.domain.provider

interface CountryCodeProvider {
    fun getCountryCode(country: String): String?
}


