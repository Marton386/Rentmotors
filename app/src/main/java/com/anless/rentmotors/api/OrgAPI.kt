package com.anless.rentmotors.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import com.anless.rentmotors.api.entities.DadataDTO
import com.anless.rentmotors.api.requests.OrgRequest

interface OrgAPI {
    @POST("/suggestions/api/4_1/rs/suggest/party/")
    suspend fun getOrg(
        @Body orgRequest: OrgRequest,
        @Query("token") token: String
    ): Response<DadataDTO>
}