package com.anless.rentmotors.models

class Car(
    val id: String,
    val code: String,
    val model: String,
    val perDayPrice: Double,
    val price: Double,
    val currency: String,
    val deposit: Double,
    val seats: Int,
    val doors: Int,
    val bag: Int,
    val luggage: Int,
    val at: Boolean, // auto transmission
    val ac: Boolean, // conditioner
    val includedFeatures: List<String>,
    val mileage: Int,
    val urlImg: String,
    val onRequest: Boolean,
    val extras: List<Extra>,
    val fees: List<Fee>
)