package com.ba.halloweenmovies.data.repository

import com.ba.halloweenmovies.data.model.Film
import com.ba.halloweenmovies.data.repository.MockFilmData.allFilms
import com.ba.halloweenmovies.data.room.FilmDao
import com.ba.halloweenmovies.data.room.FilmDataEntity
import com.ba.halloweenmovies.data.room.toFilm
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


sealed class FetchError {
    data object Ok : FetchError()
    data object NetworkError : FetchError()
    data object NoDataLeftError : FetchError() // Last page reached
    data object UnexpectedError : FetchError() // Any other
}

sealed class FilmsRequestResult {
    data class Success(val films: List<Film>) : FilmsRequestResult()
    data class Error(val fetchError: FetchError) : FilmsRequestResult()
}

fun getNFilms(allFilms: Flow<List<FilmDataEntity>>, n: Int): Flow<List<FilmDataEntity>> {
    return allFilms.map { films ->
        films.take(n)
    }
}

fun mapFlowToFilmList(flow: Flow<List<FilmDataEntity>>): Flow<List<Film>> {
    return flow.map { filmEntities ->
        filmEntities.map { it.toFilm() }
    }
}

suspend fun collectFilms(flow: Flow<List<FilmDataEntity>>): List<Film> {
    val filmsFlow = mapFlowToFilmList(flow)
    var filmList: List<Film> = emptyList()
    filmsFlow.collect { films ->
        filmList = films
    }
    return filmList
}

class FilmsRepository(
    private val filmDao: FilmDao
) {
    private val filmsAmountToRequest = 6
    private var nextPosition = 0

    suspend fun getInitFilms(): FilmsRequestResult {
        delay(500)

        val allFilms: Flow<List<FilmDataEntity>> = filmDao.getAllFilms()
        val filmsCount: Int = filmDao.countFilms()

        if (filmsCount == 0) {
            filmDao.insertFilms(MockFilmData.allFilms)
        }

        val films = getNFilms(allFilms, filmsCount)
        nextPosition = filmsCount

        return if (filmsCount == 0) FilmsRequestResult.Error(FetchError.NoDataLeftError)
        else FilmsRequestResult.Success(collectFilms(films))
    }

    suspend fun getNextFilms(): FilmsRequestResult {
        delay(500)

        return FilmsRequestResult.Error(FetchError.NoDataLeftError)
    }

    suspend fun getFavouriteFilmsInit(): FilmsRequestResult {
        delay(500)

        val allFilms: Flow<List<FilmDataEntity>> = filmDao.getFavouriteFilms()
        val filmsCount: Int = filmDao.countFavouriteFilms()

        val films = getNFilms(allFilms, filmsCount)

        return if (filmsCount == 0) FilmsRequestResult.Error(FetchError.NoDataLeftError)
        else FilmsRequestResult.Success(collectFilms(films))
    }

    suspend fun getFavouriteFilmsNext(): FilmsRequestResult {
        delay(500)

        return FilmsRequestResult.Error(FetchError.NoDataLeftError)
    }

    suspend fun getFilmsByNameInit(requestString: String?): FilmsRequestResult {
        delay(500)
        val allFoundFilms = allFilms.filter { it.title.contains(requestString ?: "") }

        val resultFilms = allFoundFilms.subList(0, minOf(filmsAmountToRequest, allFoundFilms.size))
        nextPosition = resultFilms.size

        return if (resultFilms.isEmpty()) FilmsRequestResult.Error(FetchError.NoDataLeftError)
        else FilmsRequestResult.Success(resultFilms)
    }

    fun getFilmsByNameNext(requestString: String?): FilmsRequestResult {
        val allFoundFilms = allFilms.filter { it.title.contains(requestString ?: "") }

        if (nextPosition >= allFoundFilms.size)
            return FilmsRequestResult.Error(FetchError.NoDataLeftError)

        val nextBatchSize = minOf(filmsAmountToRequest, allFoundFilms.size - nextPosition)
        val resultFilms = allFoundFilms.subList(nextPosition, nextPosition + nextBatchSize)
        nextPosition += nextBatchSize

        return FilmsRequestResult.Success(resultFilms)
    }

    suspend fun isFavourite(filmId: Int): Boolean {
        val film = filmDao.getFilmById(filmId)

        return film!!.isFavourite
    }

    suspend fun toggleFavourite(filmId: Int) {
        val film = filmDao.getFilmById(filmId)
        film!!.isFavourite = !film.isFavourite

        filmDao.updateFilm(film)
    }
}
