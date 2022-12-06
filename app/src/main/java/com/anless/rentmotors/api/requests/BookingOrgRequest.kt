package com.anless.rentmotors.api.requests

data class BookingOrgRequest(
    val car_id: String,
    val client_info: BookingRequest.Client,
    val passport_info: BookingRequest.Passport?,
    val dl_info: BookingRequest.DriverLicense?,
    val extras: List<BookingRequest.OrderExtra>,
    val flight_number: String,
    val comments: String,
    val new_client: Int,
    val entity_info: EntityInfo
) {
    data class EntityInfo(
        val full_title: String,
        val short_title: String?,
        val tin: String?,
        val psrn: String,
        val iec: String?,
        val phone: String,
        val email: String,
        val manager_name: String,
        val manager_post: String,
        val legal_address: String,
        val postal_address: String
    )
}