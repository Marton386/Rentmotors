package com.anless.rentmotors.ui.searchStations

import android.view.ViewGroup
import android.view.LayoutInflater
import com.anless.rentmotors.models.Station
import androidx.recyclerview.widget.RecyclerView
import com.anless.rentmotors.databinding.ItemStationSearchBinding

class SearchStationViewHolder private constructor(
    private val binding: ItemStationSearchBinding,
    private val onClick: (Station) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup, onClick: (Station) -> Unit): SearchStationViewHolder {
            val binding =
                ItemStationSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SearchStationViewHolder(binding, onClick)
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
        binding.tvName.text = station.name
        binding.tvAddress.text = station.address
        binding.tvPhone.text = station.phone
    }
}