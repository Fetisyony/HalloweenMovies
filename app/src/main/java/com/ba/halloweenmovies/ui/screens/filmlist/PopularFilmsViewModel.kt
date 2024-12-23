package com.ba.halloweenmovies.ui.screens.filmlist

import com.ba.halloweenmovies.data.model.Film
import com.ba.halloweenmovies.data.repository.FilmsRepository
import com.ba.halloweenmovies.data.repository.FilmsRequestResult
import com.ba.halloweenmovies.ui.screens.BaseFilmListViewModel


class PopularFilmsViewModel(films: FilmsRepository) : BaseFilmListViewModel<Film>(films) {
    override suspend fun fetchItemsCore(
        refresh: Boolean, requestString: String?
    ): FilmsRequestResult {
        val newFilms = if (refresh) films.getInitFilms()
        else films.getNextFilms()
        return newFilms
    }
}
