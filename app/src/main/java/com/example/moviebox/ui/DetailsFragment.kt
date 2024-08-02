package com.example.moviebox.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.moviebox.MovieViewModel
import com.example.moviebox.R
import com.example.moviebox.databinding.FragmentDetailsBinding
import com.example.moviebox.util.NetworkConstants
import com.example.moviebox.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {
    private var binding: FragmentDetailsBinding by autoCleared()

    private val movieViewModel : MovieViewModel by viewModels()

    private val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        val movie = args.movie

        val posterUrl = NetworkConstants.IMAGE_BASE_URL + movie.poster_path

        binding.ivBackground.load(posterUrl) {
            placeholder(R.drawable.ic_generic_movie_poster)
            error(R.drawable.ic_launcher_background)
        }
        val formattedDateToDayMonthYear = movieViewModel.formatDate(movie.release_date)

        binding.tvMovieTitle.text = movie.title
        binding.tvMovieOverview.text = movie.overview
        binding.tvMovieReleaseDate.text = formattedDateToDayMonthYear
    }
}
