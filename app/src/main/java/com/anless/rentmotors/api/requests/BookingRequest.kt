package com.anless.rentmotors.api.requests

data class BookingRequest(
    val car_id: String,
    val client_info: Client,
    val passport_info: Passport?,
    val dl_info: DriverLicense?,
    val extras: List<OrderExtra>,
    val flight_number: String,
    val comments: String,
    val new_client: Int
) {
    data class Client(
        val first_name: String,
        val last_name: String,
        val patronymic: String?,
        val phone: String?,
        val email: String,
        val country: String?,
        val address: String?,
        val birthday: String?,
        val client_id: Int?
    )

    data class DriverLicense(
        val number: String?,
        val issue_date: String?
    )

    data class Passport(
        val number: String?,
        val country: String?,
        val issue: String?,
        val issue_date: String?
    )

    data class OrderExtra(
        val extras_short_code: String,
        val quantity: Int,
        val adr: String
    )
}