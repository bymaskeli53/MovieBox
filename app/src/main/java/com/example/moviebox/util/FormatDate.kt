package com.example.moviebox.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Formatting date to day month year format
 */
object FormatDate {
    fun formatDate(inputText: String): String =
        if (inputText.isNotEmpty()) {
            try {
                val inputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                val date = inputFormatter.parse(inputText)
                outputFormatter.format(date!!)
            } catch (e: ParseException) {
                "No Date"
            }
        } else {
            "No Date"
        }
}
