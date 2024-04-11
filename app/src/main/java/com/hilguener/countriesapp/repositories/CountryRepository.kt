package com.hilguener.countriesapp.repositories

import com.hilguener.countriesapp.model.Country

interface CountryRepository {
    suspend fun getAllCountries(): ArrayList<Country> {
        return arrayListOf()
    }
}
