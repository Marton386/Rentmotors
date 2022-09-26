package com.anless.rentmotors.ui.bookingDetails

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import com.anless.rentmotors.models.Extra
import androidx.recyclerview.widget.RecyclerView
import androidx.core.widget.addTextChangedListener
import com.anless.rentmotors.databinding.ItemExtraBinding

class ExtraViewHolder private constructor(
    private val binding: ItemExtraBinding, private val callback: ExtrasAdapter.OnEditExtra
) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(
            parent: ViewGroup,
            callback: ExtrasAdapter.OnEditExtra
        ): ExtraViewHolder {
            val binding =
                ItemExtraBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ExtraViewHolder(binding, callback)
        }
    }

    private var item: Extra? = null

    init {
        binding.root.setOnClickListener {
            item?.let {
                callback.onExtraClick(it)
            }

            binding.etAddress.clearFocus()
        }

        binding.etAddress.addTextChangedListener { address  ->
            item?.let {
                callback.onAddressChanged(it, address.toString().trim())
            }
        }
    }

    fun bind(extra: Extra) {
        item = extra
        binding.tvTitle.text = extra.name
        binding.tvDescription.text = extra.description

        binding.tvPrice.text = "${extra.getShowPrice()} ${extra.currency}"
        binding.tvAmount.text = extra.amount.toString()
        binding.tvAmount.isSelected = extra.isSelected()
        binding.iconSelected.isSelected = extra.isSelected()
        binding.tvPrice.isSelected = extra.isSelected()
        binding.etAddress.setText(extra.address)
        binding.etAddress.setSelection(extra.address.length)

        if (extra.isMultiSelect()) {
            binding.tvAmount.visibility = View.VISIBLE
            binding.iconSelected.visibility = View.INVISIBLE
        } else {
            binding.tvAmount.visibility = View.INVISIBLE
            binding.iconSelected.visibility = View.VISIBLE
        }

        if (extra.isInputVisible()) {
            binding.etAddress.visibility = View.VISIBLE
            binding.tvDescription.visibility = View.GONE
        } else {
            binding.etAddress.visibility = View.GONE
            binding.tvDescription.visibility = View.VISIBLE
        }
    }

    fun updateAddress(extra: Extra) {
        item = extra
    }
}