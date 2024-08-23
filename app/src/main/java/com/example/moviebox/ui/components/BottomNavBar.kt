package com.example.moviebox.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviebox.R
import com.example.moviebox.ui.theme.SpeechRed

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val bottomNavItems =
        listOf(
            NavigationItem.Movies,
            NavigationItem.Favorites,
            NavigationItem.Search,
        )

    NavigationBar(
        modifier
            .graphicsLayer {
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                )
                clip = true
            },
        containerColor = colorResource(id = R.color.black),
        contentColor = Color.Red
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                label = {
                    Text(text = item.title)
                },
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.route.name
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = Color.White.copy(0.4f),
                    unselectedTextColor = Color.White.copy(0.4f)
                ),
                selected = currentRoute == item.route.name,
                onClick = {
                    navController.navigate(item.route.name) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }

            )

        }
    }
}

// @Composable
// fun BottomNavBar(
//    items: List<BottomNavItem>,
//    onItemSelected: (BottomNavItem) -> Unit,
// ) {
//    var selectedItem by remember { mutableStateOf(items.first()) }
//
//    NavigationBar(
//        containerColor = Color.White, // Arkaplan rengi
//        contentColor = Color.Black, // Icon ve Text rengi
//    ) {
//        items.forEach { item ->
//            NavigationBarItem(
//                icon = {
//                    Icon(
//                        painter = painterResource(id = item.icon),
//                        contentDescription = item.title,
//                    )
//                },
//                label = { Text(item.title) },
//                selected = selectedItem == item,
//                onClick = {
//                    selectedItem = item
//                    onItemSelected(item)
//                },
//                colors =
//                    NavigationBarItemDefaults.colors(
//                        selectedIconColor = Color.Black,
//                        selectedTextColor = Color.Black,
//                        unselectedIconColor = Color.Gray,
//                        unselectedTextColor = Color.Gray,
//                    ),
//            )
//        }
//    }
// }

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

    val navController = rememberNavController()
    BottomNavBar(navController = navController)

}
