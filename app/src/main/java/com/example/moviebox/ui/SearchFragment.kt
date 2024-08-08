package com.example.moviebox.ui

import android.os.Bundle
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviebox.MovieAdapter2
import com.example.moviebox.R
import com.example.moviebox.Resource
import com.example.moviebox.SearchViewModel
import com.example.moviebox.databinding.FragmentSearchBinding
import com.example.moviebox.util.autoCleared
import com.example.moviebox.util.hide
import com.example.moviebox.util.hideKeyboard
import com.example.moviebox.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private var binding: FragmentSearchBinding by autoCleared()

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        // ViewCompat.requestApplyInsets(view)

        binding.searchView.setOnQueryTextListener(
            object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        searchViewModel.searchMovies(query)
                        hideKeyboard()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
            },
        )
        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.movies.collect { movies ->
                when (movies) {
                    is Resource.Error -> println("Hi")
                    is Resource.Loading -> binding.progressBar.show()
                    is Resource.Success -> {
                        binding.progressBar.hide()

                        val data = movies.data
                        for (i in data?.results?.indices!!) {
                            val formattedDateToDayMonthYear =
                                searchViewModel.formatDate(data.results[i].release_date)
                            data.results.get(i)?.release_date = formattedDateToDayMonthYear
                        }
                        val adapter = data.results.let { MovieAdapter2(it) }
                        binding.rvSearch.adapter = adapter
                        binding.rvSearch.layoutManager = GridLayoutManager(requireContext(), 2)
                    }
                }
            }
        }
    }
}
