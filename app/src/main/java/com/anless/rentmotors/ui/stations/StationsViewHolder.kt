package com.anless.rentmotors.ui.stations

import android.view.ViewGroup
import android.view.LayoutInflater
import com.anless.rentmotors.models.Station
import androidx.recyclerview.widget.RecyclerView
import com.anless.rentmotors.databinding.ItemStationBinding

class StationsViewHolder private constructor(
    private val binding: ItemStationBinding,
    private val onClick: (Station) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup, onClick: (Station) -> Unit): StationsViewHolder {
            val binding = ItemStationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return StationsViewHolder(binding, onClick)
        }
    }

    private var item: Station? = null

    init {
        itemView.setOnClickListener {
            item?.let {
                onClick.invoke(it)
            }
        }
    }

    fun bind(station: Station) {
        item = station
        binding.tvName.text = "${station.getCity()},\n${station.getLocationName()}"
        binding.tvAddress.text = station.address
        binding.tvPhone.text = station.phone
    }
}