package com.example.moviebox.util.extension

import android.os.SystemClock
import android.view.View

/**
 * On click
 *This extension function aims to prevent multiple clicks on a view in a short amount of time.
 *
 * @param debounceDuration
 * @param action
 * @receiver
 */
fun View.onClick(
    debounceDuration: Long = 300L,
    action: (View) -> Unit,
) {
    setOnClickListener(
        DebouncedOnClickListener(debounceDuration) {
            action(it)
        },
    )
}

// Extension function to set the visibility to VISIBLE
fun View.show() {
    visibility = View.VISIBLE
}

// Extension function to set the visibility to INVISIBLE
fun View.hide() {
    visibility = View.INVISIBLE
}

// Extension function to set the visibility to GONE
fun View.gone() {
    visibility = View.GONE
}

fun View.toggleVisibility() {
    visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
}

private class DebouncedOnClickListener(
    private val debounceDuration: Long,
    private val clickAction: (View) -> Unit,
) : View.OnClickListener {
    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
        val now = SystemClock.elapsedRealtime()
        if (now - lastClickTime >= debounceDuration) {
            lastClickTime = now
            clickAction(v)
        }
    }
}
