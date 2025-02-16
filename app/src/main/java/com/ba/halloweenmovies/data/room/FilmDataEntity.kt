package com.ba.halloweenmovies.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ba.halloweenmovies.data.model.Film


@Entity(tableName = "film_table")
data class FilmDataEntity(
    @PrimaryKey val tableId: Int,
    val id: Int,
    val title: String,
    val description: String,
    val rating: Float,
    val year: Int,
    val posterUrl: String,
    var isFavourite: Boolean
)


fun FilmDataEntity.toFilm(isFavourite: Boolean = false): Film {
    return Film(
        id = this.id,
        title = this.title,
        description = this.description,
        rating = this.rating,
        year = this.year,
        posterUrl = this.posterUrl,
        isFavourite = isFavourite
    )
}