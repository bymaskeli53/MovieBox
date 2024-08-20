package com.example.moviebox.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import coil.load
import com.example.moviebox.R
import com.example.moviebox.database.MovieEntity
import com.example.moviebox.databinding.FragmentDetailsBinding
import com.example.moviebox.model.Cast
import com.example.moviebox.model.mapper.MovieToMovieEntityMapper
import com.example.moviebox.ui.adapter.ActorsAdapter
import com.example.moviebox.ui.fragment.base.BaseFragment
import com.example.moviebox.util.Resource
import com.example.moviebox.util.constant.NetworkConstants
import com.example.moviebox.util.constant.ViewConstants.MAX_LINES_MOVIE_OVERVIEW
import com.example.moviebox.util.extension.hide
import com.example.moviebox.util.extension.setResizableText
import com.example.moviebox.util.extension.show
import com.example.moviebox.viewmodel.CreditsViewModel
import com.example.moviebox.viewmodel.FavoriteViewModel
import com.example.moviebox.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(FragmentDetailsBinding::inflate, R.layout.fragment_details) {
    private var isFavorite = false

    private val movieViewModel: MovieViewModel by viewModels()
    private val creditsViewModel: CreditsViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    private val args: DetailsFragmentArgs by navArgs()
    private lateinit var movieEntity: MovieEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Screen opening animation
         * İnflater
         */
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_in)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)


        movieEntity = MovieToMovieEntityMapper.map(args.movie)

        setupUI()
        observeFavoriteMovie()
        observeActors()
        setupListeners()
    }

    private fun setupUI() {
        val movie = args.movie
        binding.ivBackground.load(NetworkConstants.IMAGE_BASE_URL + movie.poster_path) {
            placeholder(R.drawable.ic_generic_movie_poster)
            error(R.drawable.ic_generic_movie_poster)
        }
        binding.tvMovieTitle.text = movie.title
        binding.tvMovieOverview.setResizableText(
            fullText = movie.overview ?: getString(R.string.no_overview),
            maxLines = MAX_LINES_MOVIE_OVERVIEW,
            viewMore = true,
        )

        binding.ratingBar.rating = movie.vote_average?.toFloat() ?: 0.0f
        binding.tvMovieReleaseDate.text = movie.release_date?.let { movieViewModel.formatDate(it) }
    }

    private fun setupListeners() {
        binding.ivStar.setOnClickListener { toggleFavorite() }
        binding.cardYoutube.setOnClickListener { openYoutubeTrailer(args.movie.id) }
    }

    private fun openYoutubeTrailer(movieId: Int) {
        movieViewModel.fetchTrailerKey(movieId)
        movieViewModel.trailerKey.observe(viewLifecycleOwner) { trailerKey ->
            if (trailerKey != null) {
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=$trailerKey"),
                    )
                startActivity(intent)
            } else {
                Toast
                    .makeText(
                        requireContext(),
                        getString(R.string.could_not_find_trailer),
                        Toast.LENGTH_SHORT,
                    ).show()
            }
        }
    }

    private fun observeFavoriteMovie() {
        favoriteViewModel.getMovieById(args.movie.id)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteViewModel.currentMovie.collect { movie ->
                    isFavorite = movie?.isFavorite == true
                    binding.ivStar.setImageResource(
                        if (isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_empty,
                    )
                }
            }
        }
    }

    private fun observeActors() {
        creditsViewModel.getActors(args.movie.id)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                creditsViewModel.actors.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val actorList = resource.data?.cast
                            updateActors(actorList)
                            binding.progressBarActors.hide()
                        }

                        is Resource.Loading -> binding.progressBarActors.show()
                        is Resource.Error -> Log.e("DetailsFragment", "Failed to load actors")
                        is Resource.Idle -> Log.e("DetailFragment", "Idle")
                    }
                }
            }
        }
    }

    private fun updateActors(actorList: List<Cast>?) {
        if (actorList != null) {
            val actorsAdapter =
                ActorsAdapter(onActorClick = { actor ->
                    val action =
                        DetailsFragmentDirections.actionDetailsFragmentToActorBottomSheetDialogFragment(
                            actor,
                        )
                    findNavController().navigate(action)
                })
            actorsAdapter.submitList(actorList)
            binding.rvActors.adapter = actorsAdapter
        }
    }

    private fun toggleFavorite() {
        binding.ivStar.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.bounce))
        if (isFavorite) {
            binding.ivStar.setImageResource(R.drawable.ic_star_empty)
        } else {
            binding.ivStar.setImageResource(R.drawable.ic_star_filled)
        }
        isFavorite = !isFavorite
        movieEntity.isFavorite = isFavorite
        favoriteViewModel.onFavoriteButtonClick(movieEntity)
    }
}
