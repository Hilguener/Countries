package com.hilguener.countriesapp.repositories

import com.hilguener.countriesapp.api.CountryApi
import com.hilguener.countriesapp.model.Country

class CountryRepositoryImpl(private val countryApi: CountryApi) : CountryRepository {
    override suspend fun getAllCountries(): ArrayList<Country> = countryApi.getCountries()
}
