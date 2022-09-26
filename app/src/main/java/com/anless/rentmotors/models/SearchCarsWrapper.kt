package com.anless.rentmotors.models

data class SearchCarsWrapper(
    val cars: List<Car>,
    val days: Int,
    val currency: String
)
