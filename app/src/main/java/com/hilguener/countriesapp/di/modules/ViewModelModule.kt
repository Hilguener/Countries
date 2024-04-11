package com.hilguener.countriesapp.di.modules

import com.hilguener.countriesapp.repositories.CountryRepository
import com.hilguener.countriesapp.viewmodel.CountryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel {provideCountryViewModel(get())}
}

fun provideCountryViewModel(countryRepository: CountryRepository): CountryViewModel {
    return CountryViewModel(
        countryRepository = countryRepository
    )
}
