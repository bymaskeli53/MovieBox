package com.example.moviebox.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.moviebox.R
import com.example.moviebox.model.Actors
import com.example.moviebox.model.MovieItem
import com.example.moviebox.util.constant.NetworkConstants.IMAGE_BASE_URL

@Composable
fun DetailsScreen(
    movie: MovieItem,
    onStarClick: () -> Unit = {},
    onYoutubeClick: (Int) -> Unit,
    onActorClick: (Actors) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        // Background Image
        Image(
            painter = rememberAsyncImagePainter(model = IMAGE_BASE_URL + movie.backdrop_path),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)

        )

        Spacer(modifier = Modifier.height(16.dp))

        // Movie Title
        Text(
            text = movie.title ?: "No Title",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(Color.White)
                .shadow(elevation = 2.dp)
        )

        // Movie Rating
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Rating: ${movie.vote_average}",
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            movie.vote_average?.div(2)?.let {
                RatingBar(
                    rating = it.toFloat(),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Movie Overview
        Text(
            text = movie.overview ?: "No Overview Available",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(Color.White)
                .shadow(elevation = 2.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Release Date
        Text(
            text = "Release Date: ${movie.release_date}",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Actors RecyclerView (replaced with LazyRow in Compose)
//        LazyRow(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(120.dp)
//                .padding(horizontal = 16.dp)
//        ) {
//            items(movie.actors) { actor ->
//                ActorCard(actor = actor, onClick = { onActorClick(actor) })
//            }
//        }

        Spacer(modifier = Modifier.height(24.dp))

        // Youtube Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(IntrinsicSize.Min)
                    .clickable(onClick = {
                        onYoutubeClick.invoke(movie.id)
                    }),
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_youtube2),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Watch Trailer on YouTube",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Star Button
        IconButton(
            onClick = onStarClick,
            modifier = Modifier
                .size(80.dp)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = if (movie.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_empty),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Composable
fun RatingBar(rating: Float, modifier: Modifier = Modifier) {
    com.example.moviebox.ui.components.RatingBar(rating = rating.toString())
}

@Composable
fun ActorCard(actor: Actors, onClick: () -> Unit) {
    // Implement the actor card here
}


//@Preview(showBackground = true)
//@Composable
//fun DetailsScreenPreview() {
//    DetailsScreen()
//}
