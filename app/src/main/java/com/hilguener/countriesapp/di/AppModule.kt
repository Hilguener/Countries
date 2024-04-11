package com.hilguener.countriesapp.di

import com.hilguener.countriesapp.di.modules.apiModule
import com.hilguener.countriesapp.di.modules.repositoriesModule
import com.hilguener.countriesapp.di.modules.viewModelModules

val appModules = listOf(
    apiModule, repositoriesModule, viewModelModules
)