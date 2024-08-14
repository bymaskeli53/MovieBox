package com.example.moviebox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebox.databinding.ItemMovieBinding

class FavoriteAdapter : ListAdapter<MovieEntity, FavoriteAdapter.FavoriteViewHolder>(MovieDiffCallback()) {
    inner class FavoriteViewHolder(
        val binding: ItemMovieBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FavoriteViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FavoriteViewHolder,
        position: Int,
    ) {
        val movieEntity = getItem(position)
        holder.binding.movieTitleTextView.text = movieEntity.title
        holder.binding.movieImageView.setImageResource(R.drawable.ic_star_filled)
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
