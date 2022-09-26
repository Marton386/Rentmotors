package com.anless.rentmotors.models.converters

import com.anless.rentmotors.models.Station
import com.google.android.gms.maps.model.LatLng
import com.anless.rentmotors.api.entities.StationDTO

class StationConverter {
    fun toModel(remote: StationDTO) = Station(
        remote.id,
        remote.title,
        remote.address,
        remote.phone,
        remote.short_code,
        remote.GMT,
        remote.worktime.day1.start,
        remote.worktime.day1.finish,
        remote.worktime.day1.ooh_service,
        LatLng(remote.position.latitude, remote.position.longitude)
    )
}