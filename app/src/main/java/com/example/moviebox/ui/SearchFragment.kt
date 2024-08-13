package com.example.moviebox.ui

import android.os.Bundle
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviebox.MovieViewModel
import com.example.moviebox.R
import com.example.moviebox.Resource
import com.example.moviebox.SearchMovieAdapter
import com.example.moviebox.SearchViewModel
import com.example.moviebox.databinding.FragmentSearchBinding
import com.example.moviebox.util.autoCleared
import com.example.moviebox.util.hide
import com.example.moviebox.util.hideKeyboard
import com.example.moviebox.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private var binding: FragmentSearchBinding by autoCleared()

    private val searchViewModel: SearchViewModel by viewModels()

    private val movieViewModel: MovieViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        // ViewCompat.requestApplyInsets(view)

        movieViewModel.getFavoriteMovieIds()
        // TODO: Make search more efficient to show favorites later
        binding.searchView.setOnQueryTextListener(
            object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        searchViewModel.searchMovies(query)
                        hideKeyboard()
                        binding.searchView.clearFocus()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
            },
        )
        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.movies.collectLatest { movies ->
                when (movies) {
                    is Resource.Error -> throw Exception(movies.exception)
                    is Resource.Loading -> binding.progressBar.show()
                    is Resource.Success -> {
                        binding.progressBar.hide()

                        val data = movies.data

                        if (data.results.isNotEmpty()) {
                            binding.rvSearch.show()
                            binding.tvNoMovieFound.hide()
                            for (i in data?.results?.indices!!) {
                                val formattedDateToDayMonthYear =
                                    searchViewModel.formatDate(data.results[i].release_date)
                                data.results.get(i)?.release_date = formattedDateToDayMonthYear
                            }
                            val adapter =
                                data.results.let {
                                    SearchMovieAdapter {
                                        val action =
                                            SearchFragmentDirections.actionSearchFragmentToDetailsFragment(
                                                it,
                                            )
                                        findNavController().navigate(action)
                                    }
                                }
                            adapter.submitList(data.results)
                            binding.rvSearch.adapter = adapter
                            binding.rvSearch.layoutManager = GridLayoutManager(requireContext(), 2)
                            movieViewModel.favoriteMovieIds.collectLatest { favoriteMovieIds ->
                                data.results.forEach { movie ->
                                    movie.isFavorite = favoriteMovieIds.contains(movie.id)
                                }
                            }
                        } else {
                            binding.rvSearch.hide()
                            binding.tvNoMovieFound.show()
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}
