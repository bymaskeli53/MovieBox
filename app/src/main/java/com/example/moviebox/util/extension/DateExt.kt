package com.example.moviebox.util.extension

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatBirthday(dateString: String): String {
    val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US)
    val outputFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("tr", "TR"))

    val date = LocalDate.parse(dateString, apiFormat)
    return date.format(outputFormat)
}