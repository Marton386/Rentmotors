package com.anless.rentmotors.ui.bookingDetails

import android.view.ViewGroup
import com.anless.rentmotors.models.Extra
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ExtrasAdapter(private val callback: OnEditExtra) :
    ListAdapter<Extra, ExtraViewHolder>(ExtraDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtraViewHolder {
        return ExtraViewHolder.create(parent, callback)
    }

    override fun onBindViewHolder(holder: ExtraViewHolder, position: Int) {
        val extra = getItem(position)
        holder.bind(extra)
    }

    override fun onBindViewHolder(
        holder: ExtraViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val extra = getItem(position)
            val payloadsObject = payloads[0] as ExtraDiffCallback.Payloads
            if (payloadsObject.hasChanges()) {
                if (payloadsObject.addressChanged) {
                    holder.updateAddress(extra)
                }
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    class ExtraDiffCallback : DiffUtil.ItemCallback<Extra>() {
        data class Payloads(
            var addressChanged: Boolean = false
        ) {
            fun hasChanges() = addressChanged
        }

        override fun areItemsTheSame(oldItem: Extra, newItem: Extra): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: Extra, newItem: Extra): Boolean {
            return oldItem.amount == newItem.amount &&
                    oldItem.address == newItem.address
        }

        override fun getChangePayload(oldItem: Extra, newItem: Extra): Any? {
            val payloads = Payloads()

            if (oldItem.address != newItem.address) {
                payloads.addressChanged = true
            }

            return if (payloads.hasChanges()) payloads else null
        }

    }

    interface OnEditExtra {
        fun onExtraClick(extra: Extra)
        fun onAddressChanged(extra: Extra, address: String)
    }
}