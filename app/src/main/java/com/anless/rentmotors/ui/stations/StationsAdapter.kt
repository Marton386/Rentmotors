package com.anless.rentmotors.ui.stations

import android.view.ViewGroup
import com.anless.rentmotors.models.Station
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class StationsAdapter(private val onClick: (Station) -> Unit) :
    ListAdapter<Station, StationsViewHolder>(StationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationsViewHolder {
        return StationsViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: StationsViewHolder, position: Int) {
        val station = getItem(position)
        holder.bind(station)
    }

    class StationDiffCallback : DiffUtil.ItemCallback<Station>() {
        override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
            return oldItem.name == newItem.name
        }
    }
}