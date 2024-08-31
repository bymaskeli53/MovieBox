package com.example.moviebox.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    var searchQuery by remember {mutableStateOf("")}

    val isSearching = remember { false }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        // SearchView equivalent
        SearchBar(
            query = searchQuery,
            onQueryChange = { newValue -> searchQuery = newValue },
            onSearch = {},
            active = true,
            onActiveChange = {},
            placeholder = {Text(text = "Search Movie ...")},
        ) {
        }
//        TextField(
//            value = searchQuery,
//            onValueChange = { newValue ->
//                // Handle the search query change
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            placeholder = { Text("Search...") },
//            colors = textFieldColors(
//                containerColor = Color.Transparent
//            )
//        )

        // RecyclerView equivalent
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(8.dp),
        ) {
            // Items can be added here
            item {
                // Example item
                Text(text = "Movie item")
            }
        }

        // ProgressBar equivalent
        if (isSearching) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        // TextView equivalent for no movie found
        if (!isSearching && /* Replace with logic to check if movies are empty */ false) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Could not find movie")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}
