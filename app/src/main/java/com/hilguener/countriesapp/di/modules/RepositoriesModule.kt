package com.hilguener.countriesapp.di.modules

import com.hilguener.countriesapp.api.CountryApi
import com.hilguener.countriesapp.repositories.CountryRepository
import com.hilguener.countriesapp.repositories.CountryRepositoryImpl
import org.koin.dsl.module

val repositoriesModule = module {

    single<CountryRepository> {
        provideCountryRepository(get())
    }
}

fun provideCountryRepository(countryApi: CountryApi): CountryRepositoryImpl {
    return CountryRepositoryImpl(
        countryApi = countryApi
    )
}
