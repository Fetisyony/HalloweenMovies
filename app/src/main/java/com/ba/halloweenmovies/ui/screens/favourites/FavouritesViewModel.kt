package com.ba.halloweenmovies.ui.screens.favourites

import com.ba.halloweenmovies.data.model.Film
import com.ba.halloweenmovies.data.repository.FilmsRepository
import com.ba.halloweenmovies.data.repository.FilmsRequestResult
import com.ba.halloweenmovies.ui.screens.BaseFilmListViewModel

class FavouritesViewModel(films: FilmsRepository) : BaseFilmListViewModel<Film>(films) {
    override suspend fun fetchItemsCore(
        refresh: Boolean, requestString: String?
    ): FilmsRequestResult {
        val newFilms = if (refresh) {
            films.getFavouriteFilmsInit()
        } else films.getFavouriteFilmsNext()

        return newFilms
    }
}
