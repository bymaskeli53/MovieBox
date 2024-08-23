package com.example.moviebox.ui.components

import com.example.moviebox.R

sealed class NavigationItem(
    val route: Route,
    val icon: Int,
    val title: String,
) {
    object Movies : NavigationItem(Route.Movies, R.drawable.ic_movie, "Movies")

    object Favorites : NavigationItem(Route.Favorites, R.drawable.ic_favorite, "Favorites")

    object Search : NavigationItem(Route.Search, R.drawable.ic_search, "Search")
}



enum class Route {
    Movies,
    Favorites,
    Search,
}
