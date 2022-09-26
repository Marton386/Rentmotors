package com.anless.rentmotors.ui.searchCars

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.anless.rentmotors.models.Car
import com.bumptech.glide.RequestManager

class CarsAdapter(private val glide: RequestManager, private val carItemListener: CarItemListener) :
    ListAdapter<Car, CarViewHolder>(CarDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        return CarViewHolder.create(parent, glide, carItemListener)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = getItem(position)
        holder.bind(car)
    }

    class CarDiffCallback : DiffUtil.ItemCallback<Car>() {
        override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem.price == newItem.price
        }

    }

    interface CarItemListener {
        fun onItemClick(car: Car)
        fun onRequestInfoClick(car: Car)
    }
}