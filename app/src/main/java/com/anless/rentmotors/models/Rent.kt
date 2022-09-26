package com.anless.rentmotors.models

class Rent(
    val numReservation: String,
    val state: Int,
    val dateFrom: Long,
    val dateTo: Long,
    val dateBooking: Long,
    val timezone: Int,
    val days: Int,
    val flightNumber: String,
    val comment: String,
    val stationFrom: Station,
    val stationTo: Station,
    val car: Car,
    val selectedExtras: List<Extra>
)