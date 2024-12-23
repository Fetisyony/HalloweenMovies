package com.ba.halloweenmovies.data.model

data class Film(
    val id: Int,
    val title: String,
    val description: String,
    val rating: Float,
    val year: Int,
    val posterUrl: String,
)
