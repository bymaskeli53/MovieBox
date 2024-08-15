package com.example.moviebox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviebox.database.MovieEntity
import com.example.moviebox.databinding.ItemFavoriteBinding
import com.example.moviebox.databinding.ItemMovieBinding
import com.example.moviebox.util.constant.DurationConstants.CROSSFADE_DURATION
import com.example.moviebox.util.constant.NetworkConstants.IMAGE_BASE_URL
import com.example.moviebox.util.extension.hide

class FavoriteAdapter : ListAdapter<MovieEntity, FavoriteAdapter.FavoriteViewHolder>(MovieDiffCallback()) {
    inner class FavoriteViewHolder(
        val binding: ItemFavoriteBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FavoriteViewHolder,
        position: Int,
    ) {
        val movieEntity = getItem(position)
        holder.binding.tvTitle.text = movieEntity.title
        holder.binding.tvOverview.text = movieEntity.overview


//        holder.binding.movieImageView.load(IMAGE_BASE_URL + movieEntity.posterPath){
//            crossfade(CROSSFADE_DURATION)
//            placeholder(R.drawable.ic_generic_movie_poster)
//            error(R.drawable.ic_generic_movie_poster)
//        }

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
