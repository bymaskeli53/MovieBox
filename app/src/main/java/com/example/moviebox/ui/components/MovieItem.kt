package com.example.moviebox.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.moviebox.R
import com.example.moviebox.model.MovieItem
import com.example.moviebox.ui.theme.RedMixedOrange
import com.example.moviebox.util.constant.NetworkConstants.IMAGE_BASE_URL

@Composable
fun MovieListItem(
    modifier: Modifier = Modifier,
    movie: MovieItem,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .background(color = RedMixedOrange),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(RedMixedOrange),
        ) {
            Column {
                Image(
                    painter =
                        rememberAsyncImagePainter
                        (model = IMAGE_BASE_URL + movie.poster_path),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier =
                        Modifier
                            .width(80.dp)
                            .height(120.dp),
                )
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = movie.title ?: "No title")
                Spacer(modifier.size(8.dp))
                Text(text = movie.release_date?.substringBefore("-") ?: "No release date")
                Spacer(modifier.size(8.dp))
                Text(text = movie.vote_average.toString())
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Image(
                    modifier =
                        Modifier
                            .width(80.dp)
                            .height(40.dp),
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = modifier.size(12.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_heart),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(24.dp)
                            .align(Alignment.End)
                            .padding(top = 8.dp),
                    alpha = if (movie.isFavorite) 1f else 0f,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListItemPreview(modifier: Modifier = Modifier) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .background(color = RedMixedOrange),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(RedMixedOrange),
        ) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.ic_heart),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier =
                        Modifier
                            .width(80.dp)
                            .height(120.dp),
                )
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "No title")
                Spacer(modifier.size(8.dp))
                Text(text = "2023-22-10".substringBefore("-") ?: "No release date")
                Spacer(modifier.size(8.dp))
                Text(text = 7.toString())
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Image(
                    modifier =
                        Modifier
                            .width(80.dp)
                            .height(40.dp),
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = modifier.size(12.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_heart),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(24.dp)
                            .align(Alignment.End)
                            .padding(top = 8.dp),
                )
            }
        }
    }
}

// @Composable
// fun MovieListItem(
//    movie: MovieItem,
//    onMovieClick: (MovieItem) -> Unit
// ) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clickable { onMovieClick(movie) }
//    ) {
//        Image(
//            painter = rememberImagePainter(IMAGE_BASE_URL + movie.poster_path) {
//                crossfade(CROSSFADE_DURATION)
//                placeholder(R.drawable.ic_generic_movie_poster)
//            },
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(180.dp)
//        )
//        Text(
//            text = movie.title,
//            style = MaterialTheme.typography.h6,
//            modifier = Modifier.padding(top = 8.dp)
//        )
//        Text(
//            text = movie.release_date?.substringBefore("-") ?: "",
//            style = MaterialTheme.typography.body2,
//            modifier = Modifier.padding(top = 4.dp)
//        )
//        Text(
//            text = String.format(Locale.getDefault(), "%.1f", movie.vote_average),
//            style = MaterialTheme.typography.body2,
//            modifier = Modifier.padding(top = 4.dp)
//        )
//        if (movie.isFavorite) {
//            Icon(
//                imageVector = Icons.Default.Favorite,
//                contentDescription = null,
//                tint = Color.Red,
//                modifier = Modifier.padding(top = 4.dp)
//            )
//        }
//    }
// }
