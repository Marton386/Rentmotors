package com.anless.rentmotors.api.entities

data class ExtraDTO(
    val title: String,
    val description: String,
    val max_count: Int,
    val per_what: Int,
    val short_code: String,
    val price: Double
)
