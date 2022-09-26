package com.anless.rentmotors.api.entities

data class StationDTO(
    val id: Int,
    val title: String,
    val short_code: String,
    val place_type: Int,
    val service_type: Int,
    val keybox: Boolean,
    val GMT: Int,
    val position: PositionDTO,
    val phone: String,
    val address: String,
    val worktime: WorktimeDTO
) {
    data class PositionDTO(
        val longitude: Double,
        val latitude: Double
    )

    data class WorktimeDTO(
        val day1: DayWorkTime
    ) {
        data class DayWorkTime(
            val start: Int,
            val finish: Int,
            val ooh_service: Boolean
        )
    }
}


