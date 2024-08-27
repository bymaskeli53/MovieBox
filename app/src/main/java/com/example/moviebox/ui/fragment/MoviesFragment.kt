package com.example.moviebox.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.moviebox.R
import com.example.moviebox.databinding.FragmentMoviesBinding
import com.example.moviebox.ui.adapter.MovieAdapter
import com.example.moviebox.ui.adapter.SearchMovieAdapter
import com.example.moviebox.ui.fragment.base.BaseFragment
import com.example.moviebox.util.NetworkConnectionLiveData
import com.example.moviebox.util.Resource
import com.example.moviebox.util.extension.gone
import com.example.moviebox.util.extension.hide
import com.example.moviebox.util.extension.hideKeyboard
import com.example.moviebox.util.extension.show
import com.example.moviebox.viewmodel.MovieViewModel
import com.example.moviebox.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment :
    BaseFragment<FragmentMoviesBinding>(FragmentMoviesBinding::inflate, R.layout.fragment_movies),
    MenuProvider {
    private lateinit var networkConnectionLiveData: NetworkConnectionLiveData

    private val movieViewModel: MovieViewModel by viewModels()
   // private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    //private lateinit var searchMovieAdapter: SearchMovieAdapter

   private lateinit var searchView: androidx.appcompat.widget.SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_in_top)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

//        searchMovieAdapter =
//            SearchMovieAdapter(onMovieClick = {
//                val action =
//                    MoviesFragmentDirections.actionMoviesFragmentToDetailsFragment(
//                        it,
//                    )
//                findNavController().navigate(action)
//            }, formatDate = { date -> searchViewModel.formatDate(date) })

        movieViewModel.getFavoriteMovieIDs()
        setupMenu()
        observeViewModel()

//        viewLifecycleOwner.lifecycleScope.launch {
//            searchViewModel.movies.collectLatest { movies ->
//                when (movies) {
//                    is Resource.Error -> {
//                        Toast
//                            .makeText(
//                                requireContext(),
//                                movies.exception.localizedMessage,
//                                Toast.LENGTH_LONG,
//                            ).show()
//                    }
//
//                    is Resource.Idle -> {
//                    }
//
//                    is Resource.Loading -> {
//                    }
//
//                    is Resource.Success -> {
//                        binding.rvSearchMovies.adapter = searchMovieAdapter
//                        val data = movies.data
//
//                        data.movieResponse.let {
//                        }
//                        searchMovieAdapter.submitList(data.movieResponse)
//                    }
//                }
//            }
//        }

        networkConnectionLiveData = NetworkConnectionLiveData(requireContext())
        networkConnectionLiveData.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                Toast
                    .makeText(
                        requireContext(),
                        getString(R.string.user_connected_to_internet),
                        Toast.LENGTH_SHORT,
                    ).show()
                movieViewModel.refreshMovies()
            } else {
                Toast
                    .makeText(
                        requireContext(),
                        getString(R.string.user_not_connected_to_internet),
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

    private fun setupRecyclerView(isGridLayout: Boolean) {
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


        binding.rvMovies.adapter = movieAdapter
        binding.rvMovies.layoutManager?.scrollToPosition(movieViewModel.position.value)

        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.movies.collectLatest { pagingData ->
                val favoriteMovieIds = movieViewModel.favoriteMovieIds.value
                val updatedPagingData =
                    pagingData.map { movie ->
                        movie.copy(isFavorite = favoriteMovieIds.contains(movie.id))
                    }
                movieAdapter.submitData(updatedPagingData)
            }
        }

        movieAdapter.addLoadStateListener { loadState ->
            when (val state = loadState.refresh) {
                is LoadState.Loading -> {
                    binding.shimmerView.show()
                    binding.shimmerView.startShimmer()
                }

                is LoadState.NotLoading -> {
                    //  saveScrollPosition()
                    binding.shimmerView.hide()
                    binding.shimmerView.stopShimmer()
                    binding.rvMovies.isVisible = movieAdapter.itemCount > 0
                    //   binding.rvMovies.layoutManager?.scrollToPosition(movieViewModel.position.value)
                }

                is LoadState.Error -> {
                    binding.shimmerView.hide()
                    binding.shimmerView.stopShimmer()
                    Toast
                        .makeText(
                            requireContext(),
                            state.error.localizedMessage,
                            Toast.LENGTH_SHORT,
                        ).show()
                }
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

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        searchView.queryHint = getString(R.string.search_movies)

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
//            if (isAdded) {
//                if (hasFocus) {
//                    searchItem.expandActionView()
//                    binding.rvMovies.gone()
//                    binding.rvSearchMovies.show()
//                } else {
//                    binding.rvMovies.show()
//                    binding.rvSearchMovies.gone()
//                }
//            }
        }

//        searchView.setOnCloseListener {
//            binding.rvMovies.show()
//            binding.rvSearchMovies.gone()
//            false
//        }

//        searchView.setOnQueryTextListener(
//            object :
//                androidx.appcompat.widget.SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                    query?.let {
//                        searchViewModel.searchMovies(it)
//                        hideKeyboard()
//                    }
//                    return true
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean = false
//            },
//        )
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.grid_recycler_view -> {
                movieViewModel.setGridLayout(true)

                setupRecyclerView(true)
                true
            }

            R.id.linear_recycler_view -> {
                movieViewModel.setGridLayout(false)
                setupRecyclerView(false)
                true
            }

            R.id.action_search -> {
                true
            }

            else -> false
        }

    override fun onDestroyView() {
        /**
         * Searchview listeners set to null to avoid memory leak
         */
        searchView.setOnQueryTextFocusChangeListener(null)
        searchView.setOnQueryTextListener(null)
        searchView.setOnCloseListener(null)
        super.onDestroyView()
    }
}
