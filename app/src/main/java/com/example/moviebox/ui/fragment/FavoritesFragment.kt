package com.example.moviebox.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.moviebox.FavoriteAdapter
import com.example.moviebox.R
import com.example.moviebox.databinding.FragmentFavoritesBinding
import com.example.moviebox.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment :
    BaseFragment<FragmentFavoritesBinding>(
        FragmentFavoritesBinding::bind,
        R.layout.fragment_favorites,
    ) {
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

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
