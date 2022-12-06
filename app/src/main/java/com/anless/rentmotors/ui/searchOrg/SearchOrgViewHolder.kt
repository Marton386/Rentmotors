package com.anless.rentmotors.ui.searchOrg

import android.view.ViewGroup
import android.view.LayoutInflater
import com.anless.rentmotors.models.Suggestion
import androidx.recyclerview.widget.RecyclerView
import com.anless.rentmotors.databinding.ItemOrgSearchBinding

class SearchOrgViewHolder private constructor(
    private val binding: ItemOrgSearchBinding,
    private val onClick: (Suggestion) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup, onClick: (Suggestion) -> Unit): SearchOrgViewHolder {
            val binding = ItemOrgSearchBinding.inflate(LayoutInflater.from(parent.context), parent,
                false)
            return SearchOrgViewHolder(binding, onClick)
        }
    }
    private var item: Suggestion? = null

    init {
        itemView.setOnClickListener {
            item?.let {
                onClick.invoke(it)
            }
        }
    }

    fun bind(suggestion: Suggestion) {
        item = suggestion
        binding.tvName.text = suggestion.data?.name?.shortWithOpf
        binding.tvInn.text = suggestion.data?.inn
        binding.tvAddress.text = suggestion.data?.address?.value
    }
}