package com.example.moviebox.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.moviebox.ActorsAdapter
import com.example.moviebox.CreditsViewModel
import com.example.moviebox.FavoriteViewModel
import com.example.moviebox.MovieEntity
import com.example.moviebox.MovieViewModel
import com.example.moviebox.R
import com.example.moviebox.Resource
import com.example.moviebox.databinding.FragmentDetailsBinding
import com.example.moviebox.model.Cast
import com.example.moviebox.util.NetworkConstants
import com.example.moviebox.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {
    private var isFavorite = false

    private var actorList: List<Cast>? = null
    private var binding: FragmentDetailsBinding by autoCleared()

    private val movieViewModel: MovieViewModel by viewModels()

    private val creditsViewModel: CreditsViewModel by viewModels()

    private val favoriteViewModel: FavoriteViewModel by viewModels()

    private val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        val movie = args.movie

        val posterUrl = NetworkConstants.IMAGE_BASE_URL + movie.poster_path

        creditsViewModel.getActors(args.movie.id)
        binding.ivBackground.load(posterUrl) {
            placeholder(R.drawable.ic_generic_movie_poster)
            error(R.drawable.ic_launcher_background)
        }
        val formattedDateToDayMonthYear = movieViewModel.formatDate(movie.release_date)

        binding.tvMovieTitle.text = movie.title
        binding.tvMovieOverview.text = movie.overview
        binding.tvMovieReleaseDate.text = formattedDateToDayMonthYear

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                creditsViewModel.actors.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            actorList = resource.data?.cast
                            val data = resource.data
                            if (data != null) {
                                val actorsAdapter = ActorsAdapter()
                                actorsAdapter.submitList(actorList)
                                binding.rvActors.adapter = actorsAdapter
                            }
                        }
                        // TODO: Bu durumlar handle edilecek
                        is Resource.Loading -> {
                            println("Loading")
                        }

                        is Resource.Error -> {
                            println("Error")
                        }
                    }
                }
            }
        }
        binding.cardYoutube.setOnClickListener {
            val movieId = args.movie.id
            movieViewModel.fetchTrailerKey(movieId)
        }

        binding.ivStar.setOnClickListener {
            toggleFavorite()
        }

        movieViewModel.trailerKey.observe(viewLifecycleOwner) { trailerKey ->
            trailerKey?.let {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$it"))
                startActivity(intent)
            }
        }
    }

    private fun toggleFavorite() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.bounce)
        val movieEntity =
            MovieEntity(
                title = args.movie.title,
                overview = args.movie.overview,
                releaseDate = args.movie.release_date,
            )
        if (isFavorite) {
            binding.ivStar.setImageResource(R.drawable.ic_star_empty)
            favoriteViewModel.deleteFavoriteMovie(movieEntity)
        } else {
            binding.ivStar.setImageResource(R.drawable.ic_star_filled)
            binding.ivStar.startAnimation(animation)
            favoriteViewModel.insertFavoriteMovie(movieEntity)
        }
        isFavorite = !isFavorite

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteViewModel.favoriteMovies.collect { movieEntity ->
                    for (movie in movieEntity) {
                        Log.d("FavoriteMovies", movie.title)
                    }
                }
            }
        }
    }
}
