package com.example.moviebox.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.moviebox.R

@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    onItemSelected: (BottomNavItem) -> Unit,
) {
    var selectedItem by remember { mutableStateOf(items.first()) }

    NavigationBar(
        containerColor = Color.White, // Arkaplan rengi
        contentColor = Color.Black, // Icon ve Text rengi
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                    )
                },
                label = { Text(item.title) },
                selected = selectedItem == item,
                onClick = {
                    selectedItem = item
                    onItemSelected(item)
                },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                    ),
            )
        }
    }
}

data class BottomNavItem(
    val title: String,
    val icon: Int,
)

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    val items =
        listOf(
            BottomNavItem("Home", R.drawable.ic_movie),
            BottomNavItem("Favorites", R.drawable.ic_favorite),
            BottomNavItem("Profile", R.drawable.ic_search),
        )
    BottomNavBar(items = items) {
        // Handle navigation item selection
    }
}
