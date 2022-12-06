package com.anless.rentmotors.ui.searchOrg

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.anless.rentmotors.models.Suggestion
import androidx.recyclerview.widget.ListAdapter

class SearchOrgAdapter(private val onClick: (Suggestion) -> Unit) :
    ListAdapter<Suggestion, SearchOrgViewHolder>(OrgDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchOrgViewHolder {
        return SearchOrgViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: SearchOrgViewHolder, position: Int) {
        val suggestion = getItem(position)
        holder.bind(suggestion)
    }

        class OrgDiffCallback : DiffUtil.ItemCallback<Suggestion>() {
        override fun areItemsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean {
            return oldItem.id == newItem.id
        }
    }
}