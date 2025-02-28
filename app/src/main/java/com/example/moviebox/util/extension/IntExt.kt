package com.example.moviebox.util.extension

import android.content.Context
import com.example.moviebox.R

/**
 * Dakika cinsinden süreyi "X saat Y dakika" formatına dönüştürür
 * Dil desteği için string resource'larını kullanır
 */
fun Int.formatRuntime(context: Context): String {
    val hours = this / 60
    val minutes = this % 60

    return when {
        hours > 0 && minutes > 0 -> {
            context.getString(R.string.format_runtime_hours_minutes, hours, minutes)
        }
        hours > 0 -> {
            context.resources.getQuantityString(R.plurals.format_runtime_hours_only, hours, hours)
        }
        else -> {
            context.resources.getQuantityString(R.plurals.format_runtime_minutes_only, minutes, minutes)
        }
    }
}