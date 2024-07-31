package com.example.moviebox.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.moviebox.MovieAdapter
import com.example.moviebox.MovieViewModel
import com.example.moviebox.R
import com.example.moviebox.Resource
import com.example.moviebox.databinding.FragmentMoviesBinding
import com.example.moviebox.util.autoCleared
import com.example.moviebox.util.hide
import com.example.moviebox.util.show

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies) {
    private var binding: FragmentMoviesBinding by autoCleared()

    val movieViewModel: MovieViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviesBinding.bind(view)

        movieViewModel.getPopularMovies()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieViewModel.movies.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val data = resource.data
                            if (data != null) {
                                val adapter = MovieAdapter()
                                adapter.submitList(data.results)
                                binding.rvMovies.adapter = adapter
                                binding.shimmerView.stopShimmer()
                                binding.shimmerView.hide()
                            }
                        }

                        is Resource.Loading -> {
                            binding.shimmerView.startShimmer()
                            binding.shimmerView.show()
                        }

                        is Resource.Error -> {
                            println(resource.message)
                        }
                    }
                }
            }
        }
    }
}
