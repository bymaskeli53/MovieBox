package com.example.moviebox.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.moviebox.viewmodel.MovieViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems


//@Composable
//fun MoviesScreen(modifier: Modifier = Modifier,viewModel: MovieViewModel = hiltViewModel()) {
//
//    val items = viewModel.movies.collectAsLazyPagingItems()
//    val state = remember { viewModel.movies }.collectAsLazyPagingItems()
//    val lazyPagingItems = pager.flow.collectAsLazyPagingItems()
//
//
//
//    LazyColumn(modifier = Modifier.fillMaxSize()) {
//        items(movie) {
//            FavoriteItem(movie = it)
//            Divider(color = Color.Gray, modifier = Modifier.height(ItemSeparatorHeight))
//        }
//    }
//}
