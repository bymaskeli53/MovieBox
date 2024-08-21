package com.example.moviebox.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moviebox.R
import com.example.moviebox.databinding.FragmentFavoritesBinding
import com.example.moviebox.ui.components.FavoriteItem
import com.example.moviebox.ui.fragment.base.BaseFragment
import com.example.moviebox.util.constant.Dimensions.ItemSeparatorHeight
import com.example.moviebox.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment :
    BaseFragment<FragmentFavoritesBinding>(
        FragmentFavoritesBinding::inflate,
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
            favoriteViewModel.favoriteMovies.collect { movie ->
                binding.composeView.setContent {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(movie) {
                            FavoriteItem(movie = it)
                            Divider(color = Color.Gray, modifier = Modifier.height(ItemSeparatorHeight))
                        }
                    }
                }
//                binding.rvFavorites.adapter =
//                    FavoriteAdapter().apply {
//                        submitList(it)
//                    }
//                binding.rvFavorites.addItemDecoration(
//                    DividerItemDecoration(
//                        requireContext(),
//                        DividerItemDecoration.VERTICAL,
//                    ),
//                )
            }
        }
    }
}
