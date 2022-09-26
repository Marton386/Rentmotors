package com.anless.rentmotors.repositories

import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import com.anless.rentmotors.api.BookAPI
import com.anless.rentmotors.api.ErrorCodes
import com.anless.rentmotors.api.ResultCallback
import com.anless.rentmotors.api.entities.BookDTO
import com.anless.rentmotors.api.requests.BookingRequest

class BookRepository(private val bookAPI: BookAPI) {
    fun book(bookingRequest: BookingRequest, callback: ResultCallback<BookDTO>) {
        bookAPI.book(bookingRequest).enqueue(object : Callback<BookDTO> {
            override fun onResponse(call: Call<BookDTO>, response: Response<BookDTO>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        callback.onDataResult(response.body()!!)
                    } else {
                        callback.onError(ErrorCodes.FAILURE)
                    }
                } else {
                    callback.onError(response.code())
                }
            }

            override fun onFailure(call: Call<BookDTO>, t: Throwable) {
                callback.onError(ErrorCodes.REQUEST_FAILURE)
            }
        })
    }
}