package com.anless.rentmotors.api.entities

data class CarDTO(
    val car_id: String,
    val car_info: CarInfo,
    val on_request: Boolean,
    val fees: List<FeeDTO>,
    val extras: List<ExtraDTO>,
    val net_rate: Double,
    val included_features: List<String>,
    val mileage: Int
) {
    data class CarInfo(
        val code: String,
        val at: Int,
        val ac: Int,
        val seats: Int,
        val doors: Int,
        val bag_small: Int,
        val bag_big: Int,
        val car_name: String,
        val car_url_transparent: String,
        val deposit: Double
    )
}
