package com.ba.halloweenmovies.ui.screens.search

import com.ba.halloweenmovies.data.model.Film
import com.ba.halloweenmovies.data.repository.FilmsRepository
import com.ba.halloweenmovies.data.repository.FilmsRequestResult
import com.ba.halloweenmovies.ui.screens.BaseFilmListViewModel


class SearchResultsViewModel(films: FilmsRepository, requestString: String?) :
    BaseFilmListViewModel<Film>(films, requestString) {
    override suspend fun fetchItemsCore(
        refresh: Boolean, requestString: String?
    ): FilmsRequestResult {
        val newFilms = if (refresh) films.getFilmsByNameInit(requestString)
        else films.getFilmsByNameNext(requestString)
        return newFilms
    }
}
