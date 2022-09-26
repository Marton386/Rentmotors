package com.anless.rentmotors.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.anless.rentmotors.api.entities.ClientInfoDTO

interface CheckClientAPI {
    @GET("https://rentmotors.ru/appconfig/rmadmin/php/checkclient_2.php")
    fun checkClient(
        @Query("fam") lastName: String,
        @Query("email") email: String
    ): Call<ClientInfoDTO>
}