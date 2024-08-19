package com.example.moviebox.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviebox.R
import com.example.moviebox.databinding.ItemSearchMovieBinding
import com.example.moviebox.model.MovieItem
import com.example.moviebox.util.constant.NetworkConstants.IMAGE_BASE_URL
import com.example.moviebox.util.extension.gone
import com.example.moviebox.util.extension.show

class SearchMovieAdapter(
    private val onMovieClick: (MovieItem) -> Unit = {},
    private val formatDate: (String) -> String,
) : ListAdapter<MovieItem, SearchMovieAdapter.SearchMovieViewHolder>(SearchMovieDiffCallback()) {
    inner class SearchMovieViewHolder(
        val binding: ItemSearchMovieBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        fun bind(movie: MovieItem) {
            binding.textViewMovieTitle.text = movie.title
            binding.imageViewMoviePoster.load(IMAGE_BASE_URL + movie.poster_path) {
                crossfade(true)
                placeholder(R.drawable.ic_generic_movie_poster)
            }
            binding.textViewMovieDate.text =
                formatDate(movie.release_date ?: context.getString(R.string.no_release_date))

            binding.root.setOnClickListener {
                onMovieClick(movie)
            }
            if (movie.isFavorite) {
                binding.ivFavorite.show()
            } else {
                binding.ivFavorite.gone()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchMovieViewHolder {
        val binding =
            ItemSearchMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchMovieViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchMovieViewHolder,
        position: Int,
    ) {
        val movie = getItem(position)
        holder.bind(movie)
    }
}

class SearchMovieDiffCallback : DiffUtil.ItemCallback<MovieItem>() {
    override fun areItemsTheSame(
        oldItem: MovieItem,
        newItem: MovieItem,
    ): Boolean {
        // Check if items are the same based on their unique identifier (e.g., ID)
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: MovieItem,
        newItem: MovieItem,
    ): Boolean {
        // Check if the content of the items is the same
        return oldItem == newItem && oldItem.isFavorite == newItem.isFavorite
    }
}
