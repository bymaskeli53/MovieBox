package com.example.moviebox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebox.databinding.ItemMovieBinding
import com.example.moviebox.model.Movie
import com.example.moviebox.model.Result

class MovieAdapter2(val movies: List<Result>) : RecyclerView.Adapter<MovieAdapter2.MovieViewHolder2>() {

    inner class MovieViewHolder2( val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder2 {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieViewHolder2(binding)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder2, position: Int) {
        holder.binding.movieTitleTextView.text = movies[position].title
        holder.binding.movieImageView.setImageResource(R.drawable.ic_youtube)
    }
}