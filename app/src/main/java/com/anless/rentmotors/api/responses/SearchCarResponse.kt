package com.anless.rentmotors.api.responses

import com.anless.rentmotors.api.entities.CarDTO
import com.anless.rentmotors.api.entities.CurrencyDTO

data class SearchCarResponse(
    val cars: List<CarDTO>,
    val days: Int,
    val currency: CurrencyDTO
)