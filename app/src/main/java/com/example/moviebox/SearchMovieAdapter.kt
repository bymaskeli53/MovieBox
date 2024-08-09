package com.example.moviebox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviebox.databinding.ItemSearchMovieBinding
import com.example.moviebox.model.Result
import com.example.moviebox.util.NetworkConstants.IMAGE_BASE_URL

class SearchMovieAdapter(
    val movies: List<Result>,
) : RecyclerView.Adapter<SearchMovieAdapter.SearchMovieViewHolder>() {
    inner class SearchMovieViewHolder(
        val binding: ItemSearchMovieBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchMovieViewHolder {
        val binding =
            ItemSearchMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchMovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(
        holder: SearchMovieViewHolder,
        position: Int,
    ) {
        holder.binding.textViewMovieTitle.text = movies[position].title
        holder.binding.imageViewMoviePoster.load(IMAGE_BASE_URL + movies[position].poster_path) {
            crossfade(true)
            placeholder(R.drawable.ic_generic_movie_poster)
        }
        holder.binding.textViewMovieDate.text = movies[position].release_date
    }
}
