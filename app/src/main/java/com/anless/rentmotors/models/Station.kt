package com.anless.rentmotors.models

import com.google.android.gms.maps.model.LatLng

class Station(
    val id: Int,
    val name: String,
    val address: String,
    val phone: String,
    val shortCode: String,
    val timezone: Int,
    val workHourStart: Int,
    val workHourEnd: Int,
    val hasKeyBox: Boolean,
    val location: LatLng,
) {
    companion object {
        const val MAIN_STATION_ID = 2
    }

    fun getCity(): String {
        val stationName = name.split(", ")
        return stationName[0]
    }

    fun getLocationName(): String {
        val stationName = name.split(", ")
        return stationName[1]
    }
}