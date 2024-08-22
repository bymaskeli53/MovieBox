package com.example.moviebox.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.example.moviebox.R
import com.example.moviebox.model.MovieItem
import com.example.moviebox.util.constant.NetworkConstants.IMAGE_BASE_URL

@Suppress("ktlint:standard:function-naming")
@Composable
fun MovieCard(
    movie: MovieItem,
    onMovieClick: (MovieItem) -> Unit,
) {
    Card(
        onClick = { onMovieClick(movie) },
        modifier =
            Modifier
                .fillMaxWidth()
                .height(200.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Red),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(model = IMAGE_BASE_URL + movie.poster_path, contentDescription = null)
//            Image(
//                painter = painterResource(id = R.drawable.ic_heart),
//                contentDescription = null,
//            )

            Spacer(modifier = Modifier.size(8.dp))

            Column {
                Text(text = movie.title ?: "No title")
                Spacer(modifier = Modifier.size(12.dp))

                Text(text = movie.release_date ?: "No release date")
                Spacer(modifier = Modifier.size(12.dp))

                Text(text = movie.vote_average.toString())
            }
            Spacer(modifier = Modifier.size(36.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_right),
                        contentDescription = null,
                        modifier =
                            Modifier
                                .align(Alignment.CenterEnd)
                                .size(width = 80.dp, height = 40.dp),
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_heart),
                        contentDescription = null,
                        modifier =
                            Modifier
                                .align(Alignment.BottomCenter)
                                .size(24.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieCardPreview(modifier: Modifier = Modifier) {
    val dummyMovie =
        MovieItem(
            title = "Example Movie",
            overview = "This is very nice movie",
            release_date = "22.03.2000",
            isFavorite = true,
            id = 1,
        )
    MovieCard(movie = dummyMovie, onMovieClick = {})
}

// @Preview(showBackground = true)
// @Composable
// fun FavoriteItemPreview() {
//    // Dummy data for preview
//    val dummyMovie =
//        MovieEntity(title = "Example Movie", overview = "This is very nice movie", releaseDate = "")
//    FavoriteItem(movie = dummyMovie)
// }
