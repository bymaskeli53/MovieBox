package com.example.moviebox.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviebox.MovieAdapter
import com.example.moviebox.MovieViewModel
import com.example.moviebox.R
import com.example.moviebox.Resource
import com.example.moviebox.databinding.FragmentMoviesBinding
import com.example.moviebox.model.Result
import com.example.moviebox.util.autoCleared
import com.example.moviebox.util.hide
import com.example.moviebox.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment :
    Fragment(R.layout.fragment_movies),
    MenuProvider {
    private var binding: FragmentMoviesBinding by autoCleared()

    private var isGridLayout = false

    val movieViewModel: MovieViewModel by viewModels()

    private lateinit var movieAdapter: MovieAdapter

    private var currentMovieList: List<Result>? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviesBinding.bind(view)

        movieViewModel.getPopularMovies()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setUpRecyclerView(isGridLayout)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieViewModel.movies.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            currentMovieList = resource.data?.results
                            val data = resource.data
                            if (data != null) {
                                movieAdapter.submitList(data.results)
                                binding.rvMovies.adapter = movieAdapter
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

    private fun setUpRecyclerView(isGridLayout: Boolean) {
        movieAdapter = MovieAdapter(isGridLayout)
        binding.rvMovies.layoutManager =
            if (isGridLayout) {
                GridLayoutManager(requireContext(), 3)
            } else {
                LinearLayoutManager(requireContext())
            }
        binding.rvMovies.adapter = movieAdapter

        currentMovieList?.let {
            movieAdapter.submitList(it)
        }
    }

    override fun onCreateMenu(
        menu: Menu,
        menuInflater: MenuInflater,
    ) {
        menuInflater.inflate(R.menu.menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.grid_recycler_view -> {
                isGridLayout = true
                setUpRecyclerView(isGridLayout)
                return true
            }

            // TODO: This way will be changed after code refactoring because this is not extendable
            R.id.linear_recycler_view -> {
                isGridLayout = false
                setUpRecyclerView(isGridLayout)
                return true
            }

            else -> {
                return false
            }
        }
    }
}
