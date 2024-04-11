package com.hilguener.countriesapp.api

import com.hilguener.countriesapp.model.CountryResponse
import retrofit2.http.GET

interface CountryApi {
    @GET("all")
    suspend fun getCountries(): CountryResponse
}
