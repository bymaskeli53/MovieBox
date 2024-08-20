package com.example.moviebox.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebox.databinding.*

// Generic Base Adapter
abstract class BaseAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val onItemClick: (T) -> Unit,
) : ListAdapter<T, VH>(diffCallback) {
    override fun onBindViewHolder(
        holder: VH,
        position: Int,
    ) {
        val item = getItem(position)
        if (item != null) {
            bind(holder, item)
        }
    }

    abstract fun bind(
        holder: VH,
        item: T,
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): VH {
        val inflater = LayoutInflater.from(parent.context)
        return createViewHolder(inflater, parent, viewType)
    }

    abstract fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int,
    ): VH
}
