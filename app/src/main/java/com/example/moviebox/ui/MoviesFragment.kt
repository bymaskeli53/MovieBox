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
import androidx.paging.map
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviebox.MovieAdapter
import com.example.moviebox.MovieViewModel
import com.example.moviebox.R
import com.example.moviebox.databinding.FragmentMoviesBinding
import com.example.moviebox.util.NetworkConnectionLiveData
import com.example.moviebox.util.autoCleared
import com.example.moviebox.util.hide
import com.example.moviebox.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment :
    Fragment(R.layout.fragment_movies),
    MenuProvider {
    private var binding: FragmentMoviesBinding by autoCleared()
    private lateinit var networkConnectionLiveData: NetworkConnectionLiveData

    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviesBinding.bind(view)

        movieViewModel.getFavoriteMovieIds()
        setupMenu()
        observeViewModel()

        setupRecyclerView()

        networkConnectionLiveData = NetworkConnectionLiveData(requireContext())
        networkConnectionLiveData.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                Toast
                    .makeText(
                        requireContext(),
                        getString(R.string.user_connected_to_internet),
                        Toast.LENGTH_SHORT,
                    ).show()
            }
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieViewModel.movies.collectLatest { pagingData ->
                    movieViewModel.favoriteMovieIds.collectLatest { favoriteMovieIds ->
                        val updatedPagingData =
                            pagingData.map { movie ->
                                movie.copy(isFavorite = favoriteMovieIds.contains(movie.id))
                            }
                        movieAdapter.submitData(updatedPagingData)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.isGridLayout.collectLatest { isGridLayout ->
                setupRecyclerView(isGridLayout)
            }
        }
    }

    private fun setupRecyclerView(isGridLayout: Boolean = movieViewModel.isGridLayout.value) {
        movieAdapter =
            MovieAdapter(isGridLayout) { movie ->
                saveScrollPosition()
                val action = MoviesFragmentDirections.actionMoviesFragmentToDetailsFragment(movie)
                findNavController().navigate(action)
            }

        binding.rvMovies.layoutManager =
            if (isGridLayout) {
                GridLayoutManager(requireContext(), 3)
            } else {
                LinearLayoutManager(requireContext())
            }

        binding.rvMovies.itemAnimator = DefaultItemAnimator()
        binding.rvMovies.adapter = movieAdapter
        binding.rvMovies.layoutManager?.scrollToPosition(movieViewModel.position.value)

        movieAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is androidx.paging.LoadState.Loading) {
                binding.shimmerView.show()
                binding.shimmerView.startShimmer()
            } else {
                binding.shimmerView.hide()
                binding.shimmerView.stopShimmer()
            }
        }
    }

    private fun saveScrollPosition() {
        val layoutManager = binding.rvMovies.layoutManager
        val savedScrollPosition =
            when (layoutManager) {
                is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
                is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
                else -> 0
            }
        movieViewModel.setItemPosition(savedScrollPosition)
    }

    override fun onCreateMenu(
        menu: Menu,
        menuInflater: MenuInflater,
    ) {
        menuInflater.inflate(R.menu.menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.grid_recycler_view -> {
                movieViewModel.setGridLayout(true)
                true
            }

            R.id.linear_recycler_view -> {
                movieViewModel.setGridLayout(false)
                true
            }

            else -> false
        }
}
