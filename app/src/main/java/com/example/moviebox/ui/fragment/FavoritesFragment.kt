package com.example.moviebox.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.moviebox.FavoriteAdapter
import com.example.moviebox.R
import com.example.moviebox.databinding.FragmentFavoritesBinding
import com.example.moviebox.util.autoCleared
import com.example.moviebox.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private var binding: FragmentFavoritesBinding by autoCleared()

    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)
        favoriteViewModel.getFavoriteMovies()

        viewLifecycleOwner.lifecycleScope.launch {
            favoriteViewModel.favoriteMovies.collect {
                binding.rvFavorites.adapter =
                    FavoriteAdapter().apply {
                        submitList(it)
                    }
                binding.rvFavorites.addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL,
                    ),
                )
            }
        }
    }
}
