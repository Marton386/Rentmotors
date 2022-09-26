package com.anless.rentmotors.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url
import okhttp3.ResponseBody

interface VoucherAPI {
    @GET
    fun getVoucher(
        @Url url: String
    ): Call<ResponseBody>
}