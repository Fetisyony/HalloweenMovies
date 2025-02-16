package com.ba.halloweenmovies.data.model

import com.ba.halloweenmovies.data.room.FilmDataEntity

data class Film(
    val id: Int,
    val title: String,
    val description: String,
    val rating: Float,
    val year: Int,
    val posterUrl: String,
    var isFavourite: Boolean = false
)
