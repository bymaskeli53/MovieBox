package com.example.moviebox.model.mapper

// Interface mapper
// since this interface will only have 1 abstract function
// we can use fun interface
fun interface Mapper<in From, out To> {
    fun map(from: From): To
}

fun <F, T> Mapper<F, T>.mapAll(list: List<F>) = list.map { map(it) }
