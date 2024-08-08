package com.example.moviebox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviebox.databinding.ItemMovieBinding
import com.example.moviebox.databinding.ItemSearchMovieBinding
import com.example.moviebox.model.Movie
import com.example.moviebox.model.Result
import com.example.moviebox.util.NetworkConstants.IMAGE_BASE_URL

class MovieAdapter2(val movies: List<Result>) : RecyclerView.Adapter<MovieAdapter2.MovieViewHolder2>() {

    inner class MovieViewHolder2( val binding: ItemSearchMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder2 {
        val binding = ItemSearchMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieViewHolder2(binding)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder2, position: Int) {
        holder.binding.textViewMovieTitle.text = movies[position].title
        holder.binding.imageViewMoviePoster.load(IMAGE_BASE_URL + movies[position].poster_path){
            crossfade(true)
            placeholder(R.drawable.ic_generic_movie_poster)

        }
        holder.binding.textViewMovieDate.text = movies[position].release_date
    }
}