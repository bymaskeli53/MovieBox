package com.example.moviebox.util.extension


import android.view.View


fun View.show() {
    visibility = View.VISIBLE
}


fun View.hide() {
    visibility = View.INVISIBLE
}


fun View.gone() {
    visibility = View.GONE
}

fun View.toggleVisibility() {
    visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
}


