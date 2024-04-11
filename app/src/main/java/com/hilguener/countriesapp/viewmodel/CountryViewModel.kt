package com.hilguener.countriesapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilguener.countriesapp.model.Country
import com.hilguener.countriesapp.repositories.CountryRepository
import kotlinx.coroutines.launch

class CountryViewModel(private val countryRepository: CountryRepository) : ViewModel() {
    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> get() = _countries

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        fetchCountries()
    }

    fun fetchCountries() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val countries = countryRepository.getAllCountries()
                val sortedCountries = countries.sortedBy { it.name.official }
                _countries.value = sortedCountries
            } catch (e: Exception) {
                Log.e("CountryViewModel", "Erro ao buscar países: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
