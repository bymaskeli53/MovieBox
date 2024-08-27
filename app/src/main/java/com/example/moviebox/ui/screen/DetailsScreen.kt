package com.example.moviebox.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviebox.model.MovieItem

@Composable
fun DetailsScreen(modifier: Modifier = Modifier,movieItem: MovieItem) {
    Column(modifier = modifier.fillMaxSize()) {

            Text(
                text = movieItem.title ?: "No title",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = modifier.shadow(elevation = 2.dp)

            )

            Spacer(modifier = modifier.size(16.dp))

        Column {
            Text(text = "Movie Ratings",fontFamily = FontFamily.SansSerif,modifier = modifier.padding(16.dp))

        }

        // TODO: Rating bar eklenecek.


    }
}

//@Preview(showBackground = true)
//@Composable
//fun DetailsScreenPreview() {
//    DetailsScreen()
//}
