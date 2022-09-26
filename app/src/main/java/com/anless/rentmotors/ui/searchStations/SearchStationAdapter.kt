package com.anless.rentmotors.ui.searchStations

import android.view.ViewGroup
import com.anless.rentmotors.models.Station
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class SearchStationAdapter(private val onClick: (Station) -> Unit) :
    ListAdapter<Station, SearchStationViewHolder>(StationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchStationViewHolder {
        return SearchStationViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: SearchStationViewHolder, position: Int) {
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