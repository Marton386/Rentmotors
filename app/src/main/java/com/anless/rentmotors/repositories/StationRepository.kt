package com.anless.rentmotors.repositories

import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import com.anless.rentmotors.api.ErrorCodes
import com.anless.rentmotors.api.ResultCallback
import com.anless.rentmotors.api.StationAPI
import com.anless.rentmotors.api.entities.StationDTO
import com.anless.rentmotors.models.Station
import com.anless.rentmotors.models.converters.StationConverter

class StationRepository(
    private val stationAPI: StationAPI,
    private val stationConverter: StationConverter
) {
    private val cachedStationsMap = hashMapOf<Int, Station>()

    private fun saveToCache(items: List<Station>) {
        cachedStationsMap.clear()
        items.forEach {
            cachedStationsMap[it.id] = it
        }
    }

    fun getStations(callback: ResultCallback<List<Station>>) {
        if (cachedStationsMap.isNotEmpty()) {
            callback.onDataResult(cachedStationsMap.values.toList())
        } else {
            stationAPI.getStations().enqueue(object : Callback<List<StationDTO>> {
                override fun onResponse(
                    call: Call<List<StationDTO>>,
                    response: Response<List<StationDTO>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val stations = response.body()!!.map {
                                stationConverter.toModel(it)
                            }
                            saveToCache(stations)
                            callback.onDataResult(stations)
                        } else {
                            callback.onError(ErrorCodes.FAILURE)
                        }
                    } else {
                        callback.onError(response.code())
                    }
                }

                override fun onFailure(call: Call<List<StationDTO>>, t: Throwable) {
                    t.printStackTrace()
                    callback.onError(ErrorCodes.REQUEST_FAILURE)
                }
            })
        }
    }

    fun getStation(id: Int, callback: ResultCallback<Station?>) {
        if (cachedStationsMap.isEmpty()) {
            getStations (object : ResultCallback<List<Station>> {
                override fun onDataResult(data: List<Station>) {
                    callback.onDataResult(cachedStationsMap[id])
                }

                override fun onError(code: Int) {
                    callback.onError(code)
                }
            })
        } else {
            callback.onDataResult(cachedStationsMap[id])
        }
    }
}