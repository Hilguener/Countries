package com.hilguener.countriesapp.model

data class Country(
    val area: Double,
    val borders: List<String>,
    val capital: List<String>,
    val currencies: Map<String, Currency>,
    val coatOfArms: CoatOfArms,
    val demonyms: Demonyms,
    val fifa: String,
    val languages: Languages,
    val name: Name,
    val flags: Flags,
    val population: Int,
    val region: String,
    val status: String,
    val subregion: String,
)
