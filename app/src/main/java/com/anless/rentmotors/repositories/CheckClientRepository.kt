package com.anless.rentmotors.repositories

import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import com.anless.rentmotors.api.ErrorCodes
import com.anless.rentmotors.api.CheckClientAPI
import com.anless.rentmotors.api.ResultCallback
import com.anless.rentmotors.api.entities.ClientInfoDTO

class CheckClientRepository(private val checkClientAPI: CheckClientAPI) {
    fun checkClient(lastName:String, email: String, callback: ResultCallback<ClientInfoDTO>){
        checkClientAPI.checkClient(lastName, email).enqueue(object : Callback<ClientInfoDTO> {
            override fun onResponse(call: Call<ClientInfoDTO>, response: Response<ClientInfoDTO>) {
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

            override fun onFailure(call: Call<ClientInfoDTO>, t: Throwable) {
                callback.onError(ErrorCodes.REQUEST_FAILURE)
            }
        })
    }
}