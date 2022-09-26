package com.anless.rentmotors.models.converters

import com.anless.rentmotors.models.Car
import com.anless.rentmotors.models.Fee
import com.anless.rentmotors.models.Extra
import com.anless.rentmotors.api.entities.CarDTO
import com.anless.rentmotors.api.entities.FeeDTO
import com.anless.rentmotors.api.entities.ExtraDTO

class CarConverter {
    fun toModel(remote: CarDTO, days: Int, currency: String) = Car(
        remote.car_id,
        remote.car_info.code,
        remote.car_info.car_name,
        remote.net_rate / days,
        remote.net_rate,
        currency,
        remote.car_info.deposit,
        remote.car_info.seats,
        remote.car_info.doors,
        remote.car_info.bag_small,
        remote.car_info.bag_big,
        remote.car_info.at == 1,
        remote.car_info.ac == 1,
        remote.included_features,
        remote.mileage,
        remote.car_info.car_url_transparent,
        remote.on_request,
        remote.extras.map { extraToModel(it, currency) },
        remote.fees.map { feeToModel(it, currency) }
    )

    private fun extraToModel(remote: ExtraDTO, currency: String) = Extra(
        remote.short_code,
        remote.title,
        remote.description,
        remote.price,
        currency,
        0,
        remote.max_count,
        "",
        remote.per_what == 1
    )

    private fun feeToModel(remote: FeeDTO, currency: String) = Fee(
        remote.code,
        remote.description,
        remote.cost,
        currency
    )
}