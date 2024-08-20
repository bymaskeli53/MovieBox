package com.example.moviebox.ui.fragment

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
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.moviebox.R
import com.example.moviebox.databinding.FragmentSearchBinding
import com.example.moviebox.ui.adapter.SearchMovieAdapter
import com.example.moviebox.ui.fragment.base.BaseFragment
import com.example.moviebox.util.Resource
import com.example.moviebox.util.extension.hide
import com.example.moviebox.util.extension.hideKeyboard
import com.example.moviebox.util.extension.show
import com.example.moviebox.viewmodel.MovieViewModel
import com.example.moviebox.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate, R.layout.fragment_search) {


    private val searchViewModel: SearchViewModel by viewModels()

    private val movieViewModel: MovieViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)


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

        movieViewModel.getFavoriteMovieIDs()

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
                    val cursor =
                        MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                    newText?.let {
                        suggestions.forEachIndexed { index, suggestion ->
                            if (suggestion.contains(newText, true)) {
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

        binding.searchView.setOnSuggestionListener(
            object :
                android.widget.SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean = false

                @SuppressLint("Range")
                override fun onSuggestionClick(position: Int): Boolean {
                    hideKeyboard()
                    val cursor = binding.searchView.suggestionsAdapter.getItem(position) as Cursor
                    val selection =
                        cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                    binding.searchView.setQuery(selection, false)
                    return true
                }
            },
        )

        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.movies.collectLatest { movies ->
                when (movies) {
                    is Resource.Error -> {
                        binding.progressBar.hide()
                        Toast
                            .makeText(
                                requireContext(),
                                movies.exception.localizedMessage,
                                Toast.LENGTH_SHORT,
                            ).show()
                    }

                    is Resource.Loading -> binding.progressBar.show()
                    is Resource.Success -> {
                        binding.progressBar.hide()

                        val data = movies.data

                        if (data.movieResponse.isNotEmpty()) {
                            binding.rvSearch.show()
                            binding.tvNoMovieFound.hide()

                            val adapter =
                                data.movieResponse.let {
                                    SearchMovieAdapter(onMovieClick = {
                                        val action =
                                            SearchFragmentDirections.actionSearchFragmentToDetailsFragment(
                                                it,
                                            )
                                        findNavController().navigate(action)
                                    }, formatDate = { date -> searchViewModel.formatDate(date) })
                                }

                            adapter.submitList(data.movieResponse)
                            binding.rvSearch.adapter = adapter

                            binding.rvSearch.layoutManager =
                                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                            movieViewModel.favoriteMovieIds.collectLatest { favoriteMovieIds ->
                                data.movieResponse.forEach { movie ->
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
