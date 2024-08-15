package com.example.moviebox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebox.database.MovieEntity
import com.example.moviebox.databinding.ItemFavoriteBinding

class FavoriteAdapter : ListAdapter<MovieEntity, FavoriteAdapter.FavoriteViewHolder>(MovieDiffCallback()) {
    inner class FavoriteViewHolder(
        val binding: ItemFavoriteBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FavoriteViewHolder {
        val binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FavoriteViewHolder,
        position: Int,
    ) {
        val movieEntity = getItem(position)
        holder.binding.tvTitle.text = movieEntity.title
        holder.binding.tvOverview.text = movieEntity.overview
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<MovieEntity>() {
        override fun areItemsTheSame(
            oldItem: MovieEntity,
            newItem: MovieEntity,
        ): Boolean {
            return oldItem.id == newItem.id // Assuming `id` is a unique identifier for `MovieEntity`
        }

        override fun areContentsTheSame(
            oldItem: MovieEntity,
            newItem: MovieEntity,
        ): Boolean = oldItem == newItem
    }
}
