package com.example.moviebox.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.moviebox.database.MovieEntity
import com.example.moviebox.util.constant.Dimensions.ItemSeparatorHeight

@Composable
fun FavoriteScreen (modifier: Modifier = Modifier,movie: List<MovieEntity>) {

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(movie) {
            FavoriteItem(movie = it)
            Divider(color = Color.Gray, modifier = Modifier.height(ItemSeparatorHeight))
        }
    }
}
