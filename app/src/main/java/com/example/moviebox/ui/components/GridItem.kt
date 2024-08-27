package com.example.moviebox.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.moviebox.model.MovieItem
import com.example.moviebox.util.constant.NetworkConstants.IMAGE_BASE_URL
import com.example.moviebox.util.constant.ViewConstants.ASPECT_RATIO

@Composable
fun MovieGridItem(
    modifier: Modifier = Modifier,
    movie: MovieItem,
) {
    Image(
        modifier =
            Modifier
                .padding(8.dp)
                .fillMaxHeight()
                .aspectRatio(ASPECT_RATIO)
                .clip(RoundedCornerShape(8.dp)),
        painter =
            rememberAsyncImagePainter(
                model = IMAGE_BASE_URL + movie.poster_path,
            ),
        contentScale = ContentScale.Crop,
        contentDescription = "",
    )
}

// @Composable
// fun MovieGridItem(
//    movie: MovieItem,
//    onMovieClick: (MovieItem) -> Unit
// ) {
//    Box(
//        modifier = Modifier
//            .aspectRatio(2 / 3f)
//            .clickable { onMovieClick(movie) }
//    ) {
//        Image(
//            painter = rememberImagePainter(IMAGE_BASE_URL + movie.poster_path) {
//                crossfade(CROSSFADE_DURATION)
//                placeholder(R.drawable.ic_generic_movie_poster)
//            },
//            contentDescription = null,
//            modifier = Modifier.fillMaxSize()
//        )
//        if (movie.isFavorite) {
//            Icon(
//                imageVector = Icons.Default.Favorite,
//                contentDescription = null,
//                tint = Color.Red,
//                modifier = Modifier
//                    .align(Alignment.TopEnd)
//                    .padding(8.dp)
//            )
//        }
//    }
// }
