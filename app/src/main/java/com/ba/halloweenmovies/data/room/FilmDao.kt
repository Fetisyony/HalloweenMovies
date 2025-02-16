package com.ba.halloweenmovies.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ba.halloweenmovies.data.model.Film
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {
    @Query("SELECT * FROM film_table")
    fun getAllFilms(): Flow<List<FilmDataEntity>>

    @Query("SELECT * FROM film_table WHERE id = :id")
    suspend fun getFilmById(id: Int): FilmDataEntity?

    @Query("SELECT * FROM film_table WHERE isFavourite = 1")
    fun getFavouriteFilms(): Flow<List<FilmDataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilms(films: List<FilmDataEntity>)

    @Update
    suspend fun updateFilm(film: FilmDataEntity)

    @Query("SELECT COUNT(*) FROM film_table")
    suspend fun countFilms(): Int

    @Query("SELECT COUNT(*) FROM film_table WHERE isFavourite = 1")
    suspend fun countFavouriteFilms(): Int
}