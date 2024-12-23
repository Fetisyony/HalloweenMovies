package com.ba.halloweenmovies.data.repository

import com.ba.halloweenmovies.data.model.Film
import com.ba.halloweenmovies.data.repository.MockFilmData.allFilms
import kotlinx.coroutines.delay


sealed class FetchError {
    data object Ok : FetchError()
    data object NetworkError : FetchError()
    data object NoDataLeftError : FetchError() // Last page reached
    data object UnexpectedError : FetchError() // Any other
}

sealed class FilmsRequestResult {
    data class Success(val films: List<Film>) : FilmsRequestResult()
    data object Empty : FilmsRequestResult()
    data class Error(val fetchError: FetchError) : FilmsRequestResult()
}

class FilmsRepository {
    private val favouriteFilmIds = mutableSetOf<Int>()

    private val filmsAmountToRequest = 6
    private var nextPosition = 0

    fun getAllFilmsImmediately(): FilmsRequestResult {
        return FilmsRequestResult.Success(allFilms)
    }

    suspend fun getInitFilms(): FilmsRequestResult {
        delay(500)
        val films = allFilms.subList(0, minOf(filmsAmountToRequest, allFilms.size))
        nextPosition = films.size

        if (films.isEmpty())
            return FilmsRequestResult.Empty
        return FilmsRequestResult.Success(films)
    }

    suspend fun getNextFilms(): FilmsRequestResult {
        delay(500)

        if (nextPosition >= allFilms.size) {
            return FilmsRequestResult.Empty
        }

        val nextBatchSize = minOf(filmsAmountToRequest, allFilms.size - nextPosition)
        val films = allFilms.subList(nextPosition, nextPosition + nextBatchSize)
        nextPosition += nextBatchSize
        return FilmsRequestResult.Success(films)
    }

    suspend fun getFavouriteFilmsInit(): FilmsRequestResult {
        delay(500)
        val films = allFilms.filter { it.id in favouriteFilmIds }
            .subList(0, minOf(filmsAmountToRequest, favouriteFilmIds.size))
        nextPosition = films.size

        if (films.isEmpty()) {
            return FilmsRequestResult.Empty
        }
        return FilmsRequestResult.Success(films)
    }

    suspend fun getFavouriteFilmsNext(): FilmsRequestResult {
        delay(500)

        if (nextPosition >= favouriteFilmIds.size) {
            return FilmsRequestResult.Empty
        }

        val nextBatchSize = minOf(filmsAmountToRequest, favouriteFilmIds.size - nextPosition)
        val films = allFilms.filter { it.id in favouriteFilmIds }
            .subList(nextPosition, nextPosition + nextBatchSize)
        nextPosition += nextBatchSize
        return FilmsRequestResult.Success(films)
    }

    suspend fun getFilmsByNameInit(requestString: String?): FilmsRequestResult {
        delay(500)
        val allFoundFilms = allFilms.filter { it.title.contains(requestString ?: "") }

        val resultFilms = allFoundFilms.subList(0, minOf(filmsAmountToRequest, allFoundFilms.size))
        nextPosition = resultFilms.size

        if (resultFilms.isEmpty())
            return FilmsRequestResult.Empty
        return FilmsRequestResult.Success(resultFilms)
    }

    fun getFilmsByNameNext(requestString: String?): FilmsRequestResult {
        val allFoundFilms = allFilms.filter { it.title.contains(requestString ?: "") }

        if (nextPosition >= allFoundFilms.size)
            return FilmsRequestResult.Empty

        val nextBatchSize = minOf(filmsAmountToRequest, allFoundFilms.size - nextPosition)
        val resultFilms = allFoundFilms.subList(nextPosition, nextPosition + nextBatchSize)
        nextPosition += nextBatchSize

        return FilmsRequestResult.Success(resultFilms)
    }

    fun isFavourite(filmId: Int): Boolean = filmId in favouriteFilmIds

    fun toggleFavourite(filmId: Int) {
        if (isFavourite(filmId)) {
            favouriteFilmIds.remove(filmId)
        } else {
            favouriteFilmIds.add(filmId)
        }
    }
}
