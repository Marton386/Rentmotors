package com.anless.rentmotors.api

import retrofit2.Call
import retrofit2.http.GET
import com.anless.rentmotors.api.entities.StationDTO

interface StationAPI {
    @GET("/1.0/station")
    fun getStations(): Call<List<StationDTO>>
}