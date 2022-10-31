package com.anless.rentmotors.ui.searchCars

import android.view.View
import android.view.ViewGroup
import com.anless.rentmotors.R
import android.view.LayoutInflater
import com.anless.rentmotors.models.Car
import com.bumptech.glide.RequestManager
import androidx.recyclerview.widget.RecyclerView
import com.anless.rentmotors.utils.PriceFormatter
import com.anless.rentmotors.databinding.ItemCarBinding

class CarViewHolder private constructor(
    private val binding: ItemCarBinding,
    private val glide: RequestManager,
    private val carItemListener: CarsAdapter.CarItemListener
) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(
            parent: ViewGroup,
            glide: RequestManager,
            carItemListener: CarsAdapter.CarItemListener
        ): CarViewHolder {
            val binding = ItemCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CarViewHolder(binding, glide, carItemListener)
        }
    }

    private var item: Car? = null

    init {
        itemView.setOnClickListener {
            item?.let {
                carItemListener.onItemClick(it)
            }
        }

        binding.imgOnRequest.setOnClickListener {
            item?.let {
                carItemListener.onRequestInfoClick(it)
            }
        }
    }

    fun bind(car: Car) {
        item = car
        val context = itemView.context
        if (car.ev == 0) {
            binding.electricImg.visibility = View.INVISIBLE
            binding.isElectric.visibility = View.INVISIBLE
        }
        else {
            binding.similarText.visibility = View.INVISIBLE
        }
        binding.tvCarModel.text = car.model
        binding.tvAllDayCost.text = PriceFormatter.format(car.price)
        binding.tvPerDayCost.text = "${PriceFormatter.format(car.perDayPrice)} ${car.currency}"
        binding.tvDeposit.text = "${PriceFormatter.format(car.deposit)} ${car.currency}"
        binding.tvAllDayCostCur.text = car.currency
        binding.tvCarClass.text = car.code
        binding.tvAmountPlaces.text = car.seats.toString()
        binding.tvAmountDoors.text = car.doors.toString()
        binding.tvAmountBags.text = car.bag.toString()
        binding.tvAmountLuggage.text = car.luggage.toString()
        glide.load(car.urlImg)
            .placeholder(R.drawable.placeholder_car)
            .into(binding.imgCar)

        var facilities =
            if (car.at) context.getString(R.string.type_at) else context.getString(R.string.type_mt)

        if (car.ac) {
            facilities += ", ${context.getString(R.string.conditioner)}"
        }

        binding.tvFacilities.text = facilities

        binding.imgOnRequest.visibility = if (car.onRequest) View.VISIBLE else View.GONE
    }


}