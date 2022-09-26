package com.anless.rentmotors.api.entities

import com.google.gson.annotations.SerializedName

data class ClientInfoDTO(
    @SerializedName("new_client")
    val newClient: Int,
    @SerializedName("client_id")
    val idClient: Int?
)