package com.example.moviebox.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.filter
import androidx.paging.map
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.moviebox.MovieAdapter
import com.example.moviebox.MovieViewModel
import com.example.moviebox.R
import com.example.moviebox.SearchViewModel
import com.example.moviebox.databinding.FragmentMoviesBinding
import com.example.moviebox.util.NetworkConnectionLiveData
import com.example.moviebox.util.autoCleared
import com.example.moviebox.util.hide
import com.example.moviebox.util.hideKeyboard
import com.example.moviebox.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment :
    Fragment(R.layout.fragment_movies),
    MenuProvider {
    private var binding: FragmentMoviesBinding by autoCleared()
    private lateinit var networkConnectionLiveData: NetworkConnectionLiveData

    private val movieViewModel: MovieViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater  = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_in_top)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviesBinding.bind(view)

        movieViewModel.getFavoriteMovieIds()
        setupMenu()
        observeViewModel()

        setupRecyclerView(false)

//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                searchViewModel.movies.collectLatest {
//                    movieAdapter.submitData(it)
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
               // movieAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupRecyclerView(isGridLayout: Boolean) {
        movieAdapter = MovieAdapter(isGridLayout) { movie ->
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

        movieAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is androidx.paging.LoadState.Loading ||
                loadState.source.append is androidx.paging.LoadState.Loading) {
                binding.shimmerView.show()
                binding.shimmerView.startShimmer()
            } else {
                binding.shimmerView.hide()
                binding.shimmerView.stopShimmer()
            }
        }
    }

//    private fun setupRecyclerView(isGridLayout: Boolean = movieViewModel.isGridLayout.value) {
//        if (!::movieAdapter.isInitialized) {
//            movieAdapter = MovieAdapter(isGridLayout) { movie ->
//                saveScrollPosition()
//                val action = MoviesFragmentDirections.actionMoviesFragmentToDetailsFragment(movie)
//                findNavController().navigate(action)
//            }
//        }
//
//        val layoutManager = if (isGridLayout) {
//            GridLayoutManager(requireContext(), 3)
//        } else {
//            LinearLayoutManager(requireContext())
//        }
//
//        binding.rvMovies.layoutManager = layoutManager
//        binding.rvMovies.adapter = movieAdapter // Re-set adapter to force it to use the correct ViewHolder
//
//        // This ensures that data is rebound when layout changes
//        movieAdapter.notifyDataSetChanged()
//
//        binding.rvMovies.itemAnimator = DefaultItemAnimator()
//
//        // Restore scroll position
//        layoutManager.scrollToPosition(movieViewModel.position.value)
//
//        movieAdapter.addLoadStateListener { loadState ->
//            if (loadState.source.refresh is androidx.paging.LoadState.Loading ||
//                loadState.source.append is androidx.paging.LoadState.Loading) {
//                binding.shimmerView.show()
//                binding.shimmerView.startShimmer()
//            } else {
//                binding.shimmerView.hide()
//                binding.shimmerView.stopShimmer()
//            }
//        }
//    }


//    private fun setupRecyclerView(isGridLayout: Boolean = movieViewModel.isGridLayout.value) {
//        movieAdapter =
//            MovieAdapter(isGridLayout) { movie ->
//                saveScrollPosition()
//                val action = MoviesFragmentDirections.actionMoviesFragmentToDetailsFragment(movie)
//                findNavController().navigate(action)
//            }
//
//        binding.rvMovies.layoutManager =
//            if (isGridLayout) {
//                GridLayoutManager(requireContext(), 3)
//            } else {
//                LinearLayoutManager(requireContext())
//            }
//
//        binding.rvMovies.itemAnimator = DefaultItemAnimator()
//        binding.rvMovies.adapter = movieAdapter
//        binding.rvMovies.layoutManager?.scrollToPosition(movieViewModel.position.value)
//
//
//
//        movieAdapter.addLoadStateListener { loadState ->
//            if (loadState.source.refresh is androidx.paging.LoadState.Loading ||
//                loadState.source.append is androidx.paging.LoadState.Loading) {
//                binding.shimmerView.show()
//                binding.shimmerView.startShimmer()
//            } else {
//                binding.shimmerView.hide()
//                binding.shimmerView.stopShimmer()
//            }
//        }
//    }

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
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        searchView.queryHint = getString(R.string.search_movies)

//        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener
//        {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                query?.let {
//                    searchMovies(it)
//                    hideKeyboard()
//
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//
//        })
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED){
//                searchViewModel.searchMovies2("bat").collectLatest{
//                    Log.d("TAG",it.toString())
//                    movieAdapter.submitData(it)
//
//                }
//            }
//
//
//        }
//        searchView.setOnCloseListener {
//            searchItem.collapseActionView()
//            true
//        }

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
               // menuItem.collapseActionView()
                true

            }

            else -> false
        }

    private fun searchMovies(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {

            searchViewModel.searchMovies2(query).collectLatest{
                it.map {
                    Log.d("PAGE",it.title)
                }

                Log.d("SearchResults","Paging data: ${it.toString()}")
                movieAdapter.submitData(it)
                movieAdapter.notifyDataSetChanged()





            }
        }
    }
}
