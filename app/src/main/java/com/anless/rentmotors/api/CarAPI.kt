package com.anless.rentmotors.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.anless.rentmotors.api.responses.SearchCarResponse

interface CarAPI {
    @GET("/1.0/search")
    fun getCars(
        @Query("p_station") stationPickUp: Int,
        @Query("d_station") stationDropOff: Int,
        @Query("p_date") datePickUp: String,
        @Query("d_date") dateDropOff: String,
        @Query("age") age: Int = 30
    ): Call<SearchCarResponse>
}