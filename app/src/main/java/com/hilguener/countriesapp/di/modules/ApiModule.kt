package com.hilguener.countriesapp.di.modules

import com.hilguener.countriesapp.api.CountryApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val apiModule =
    module {
        single {
            provideApi()
        }
    }

fun provideApi(): CountryApi {
    return Retrofit.Builder()
        .baseUrl("https://restcountries.com/v3.1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CountryApi::class.java)
}
