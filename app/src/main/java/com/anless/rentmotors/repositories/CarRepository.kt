package com.anless.rentmotors.repositories

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.anless.rentmotors.api.CarAPI
import com.anless.rentmotors.api.ErrorCodes
import com.anless.rentmotors.api.ResultCallback
import com.anless.rentmotors.models.SearchCarsWrapper
import com.anless.rentmotors.ui.searchCars.SearchCarFilter
import com.anless.rentmotors.models.converters.CarConverter
import com.anless.rentmotors.api.responses.SearchCarResponse

class CarRepository(private val carAPI: CarAPI, private val carConverter: CarConverter) {
    fun getCars(searchCarFilter: SearchCarFilter, callback: ResultCallback<SearchCarsWrapper>) {
        carAPI.getCars(
            searchCarFilter.stationPickUp,
            searchCarFilter.stationDropOff,
            searchCarFilter.datePickUp,
            searchCarFilter.dateDropOff
        ).enqueue(object : Callback<SearchCarResponse> {
            override fun onResponse(
                call: Call<SearchCarResponse>,
                response: Response<SearchCarResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val searchCarsResponse = response.body()!!
                        val days = searchCarsResponse.days
                        val currency = searchCarsResponse.currency.short_title
                        val cars = searchCarsResponse.cars.map {
                            carConverter.toModel(it, days, currency)
                        }

                        val searchCarsWrapper = SearchCarsWrapper(cars, days, currency)
                        callback.onDataResult(searchCarsWrapper)
                    } else {
                        callback.onError(ErrorCodes.FAILURE)
                    }

                } else {
                    callback.onError(response.code())
                }
            }

            override fun onFailure(call: Call<SearchCarResponse>, t: Throwable) {
                t.printStackTrace()
                callback.onError(ErrorCodes.REQUEST_FAILURE)
            }
        })
    }
}