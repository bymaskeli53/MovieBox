package com.example.moviebox.domain

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class FormatDateUseCase
    @Inject
    constructor() {
        operator fun invoke(inputDate: String): String {
            if (inputDate.isNotEmpty()) {
                try {
                    val inputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val outputFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    val date = inputFormatter.parse(inputDate)
                    return outputFormatter.format(date!!)
                } catch (e: ParseException) {
                    return "No Date because of parse exception"
                }
            } else {
                return "No Date"
            }
        }
    }
