package com.anless.rentmotors.ui.searchCars

data class SearchCarFilter(
    val stationPickUp: Int,
    val stationDropOff: Int,
    val datePickUp: String,
    val dateDropOff: String
)
