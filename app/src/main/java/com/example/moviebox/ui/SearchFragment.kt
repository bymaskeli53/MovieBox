package com.example.moviebox.ui

import android.annotation.SuppressLint
import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.CursorAdapter
import android.widget.SearchView.OnQueryTextListener
import android.widget.SimpleCursorAdapter
import androidx.appcompat.widget.SearchView
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
        // binding.searchView.suggestionsAdapter = null
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)
        val cursorAdapter =
            SimpleCursorAdapter(
                requireContext(),
                R.layout.item_suggestion,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
            )

        val suggestions = listOf("Lord Of The Rings", "Batman", "Interstellar", "Avengers")
        binding.searchView.suggestionsAdapter = cursorAdapter


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

                override fun onQueryTextChange(newText: String?): Boolean {
                    val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                   newText?.let {
                       suggestions.forEachIndexed { index, suggestion ->
                           if (suggestion.contains(newText,true)){
                                   cursor.addRow(arrayOf(index, suggestion))
                               }
                       }
                       cursorAdapter.changeCursor(cursor)
                       return true

                   }

                    return true
                }
            },
        )

        binding.searchView.setOnSuggestionListener(object: android.widget.SearchView.OnSuggestionListener{
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            @SuppressLint("Range")
            override fun onSuggestionClick(position: Int): Boolean {
                hideKeyboard()
                val cursor = binding.searchView.suggestionsAdapter.getItem(position) as Cursor
                val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                binding.searchView.setQuery(selection, false)
                return true
            }

        })

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
