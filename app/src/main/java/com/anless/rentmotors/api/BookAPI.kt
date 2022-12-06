package com.anless.rentmotors.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.anless.rentmotors.api.entities.BookDTO
import com.anless.rentmotors.api.requests.BookingRequest
import com.anless.rentmotors.api.requests.BookingOrgRequest

interface BookAPI {
    @POST("/1.0/book")
    fun book(
        @Body bookingRequest: BookingRequest
    ): Call<BookDTO>

    @POST("/1.0/book")
    fun orgBook(
        @Body bookingOrgRequest: BookingOrgRequest
    ): Call<BookDTO>
}