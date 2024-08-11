package com.example.moviebox.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviebox.MovieAdapter
import com.example.moviebox.MovieViewModel
import com.example.moviebox.R
import com.example.moviebox.Resource
import com.example.moviebox.databinding.FragmentMoviesBinding
import com.example.moviebox.model.Result
import com.example.moviebox.util.NetworkConnectionLiveData
import com.example.moviebox.util.autoCleared
import com.example.moviebox.util.hide
import com.example.moviebox.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment :
    Fragment(R.layout.fragment_movies),
    MenuProvider {
    private var binding: FragmentMoviesBinding by autoCleared()

    private lateinit var networkConnectionLiveData: NetworkConnectionLiveData

    val movieViewModel: MovieViewModel by viewModels()

    // TODO: This will be handled to just observe
    private val isGridLayout get() = movieViewModel.isGridLayout.value

    private lateinit var movieAdapter: MovieAdapter

    private var currentMovieList: List<Result>? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviesBinding.bind(view)

        movieViewModel.getPopularMovies()
        movieViewModel.getFavoriteMovieIds()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieViewModel.movies.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            currentMovieList = resource.data?.results
                            val data = resource.data
                            if (data != null) {
                                setUpRecyclerView(isGridLayout)
                                movieViewModel.favoriteMovieIds.collect { favoriteMovieIds ->
                                    data.results.forEach { movie ->
                                        movie.isFavorite = favoriteMovieIds.contains(movie.id)
                                    }
                                    binding.shimmerView.stopShimmer()
                                    binding.shimmerView.hide()
                                }
                                movieAdapter.submitList(data.results)
                                binding.rvMovies.itemAnimator = DefaultItemAnimator()
                                binding.rvMovies.adapter = movieAdapter

                                // TODO: Uygulama tekrar çalıştırılmadan internet açıldığında veri çekilecek.

                                binding.shimmerView.stopShimmer()
                                binding.shimmerView.hide()
                            }
                        }

                        is Resource.Loading -> {
                            binding.shimmerView.startShimmer()
                            binding.shimmerView.show()

                        }

                        is Resource.Error -> {
                            Toast
                                .makeText(
                                    requireContext(),
                                    "${resource.exception.localizedMessage}",
                                    Toast.LENGTH_SHORT,
                                ).show()
                        }

                        else -> {}
                    }
                }
            }
        }

//        viewLifecycleOwner.lifecycleScope.launch {
//            movieViewModel.isGridLayout.collect{ isGrid ->
//                setUpRecyclerView(isGrid)
//
//            }
//        }
        networkConnectionLiveData = NetworkConnectionLiveData(requireContext())
        networkConnectionLiveData.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                movieViewModel.getPopularMovies()
                Toast
                    .makeText(
                        requireContext(),
                        getString(R.string.user_connected_to_internet),
                        Toast.LENGTH_SHORT,
                    ).show()
            }
        }
    }

    // TODO: Save user choice for grid or linear and save the choice in datastore and start app with previous choice
    private fun saveScrollPosition() {
        // val layoutManager = binding.rvMovies.layoutManager as LinearLayoutManager
        val layoutManager = binding.rvMovies.layoutManager

        val savedScrollPosition =
            when (layoutManager) {
                is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
                is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
                else -> 0
            }
        movieViewModel.setItemPosition(savedScrollPosition)
    }

    private fun setUpRecyclerView(isGridLayout: Boolean) {
        movieAdapter =
            MovieAdapter(isGridLayout, onMovieClick = { movie ->
                saveScrollPosition()
                val action = MoviesFragmentDirections.actionMoviesFragmentToDetailsFragment(movie)
                findNavController().navigate(action)
            })

        // TODO: Recycler view kendini yeniden yaratıyor ve her seferinde linear oluyor view modele taşımalı veya shared preferences kullanmalı
        binding.rvMovies.layoutManager =
            if (isGridLayout) {
                GridLayoutManager(requireContext(), 3)
            } else {
                LinearLayoutManager(requireContext())
            }
        binding.rvMovies.itemAnimator = DefaultItemAnimator()
        binding.rvMovies.adapter = movieAdapter

        currentMovieList?.let {
            movieAdapter.submitList(it)
        }

//        binding.rvMovies.post {
//            binding.rvMovies.layoutManager?.scrollToPosition(movieViewModel.position.value)
//        }

        binding.rvMovies.layoutManager?.scrollToPosition(movieViewModel.position.value)
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
                movieViewModel.setGridLayout(true)

                setUpRecyclerView(isGridLayout)
                return true
            }

            // TODO: This way will be changed after code refactoring because this is not extendable
            R.id.linear_recycler_view -> {
                movieViewModel.setGridLayout(false)

                setUpRecyclerView(isGridLayout)
                return true
            }

            else -> {
                return false
            }
        }
    }
}
