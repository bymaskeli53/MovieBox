package com.example.moviebox.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MovieBoxTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        // Eğer renk paletiniz, typography ve şekil (shape) dosyalarınız varsa buraya ekleyin.
        content = content
    )
}
