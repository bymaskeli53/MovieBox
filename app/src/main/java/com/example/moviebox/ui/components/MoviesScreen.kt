package com.example.moviebox.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.moviebox.util.constant.Dimensions.ItemSeparatorHeight
import com.example.moviebox.viewmodel.MovieViewModel

@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieViewModel = hiltViewModel(),
) {
    val items = viewModel.movies.collectAsLazyPagingItems()
    //val state = remember { viewModel.movies }.collectAsLazyPagingItems()


    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            count = items.itemCount,
            key = items.itemKey { it.id },
            contentType = items.itemContentType { "contentType" },
        ) { index ->
            val item = items[index]
            if (item != null) {
                MovieListItem(movie = item)
            }
            Divider(color = Color.Gray, modifier = Modifier.height(ItemSeparatorHeight))
        }
    }
}
