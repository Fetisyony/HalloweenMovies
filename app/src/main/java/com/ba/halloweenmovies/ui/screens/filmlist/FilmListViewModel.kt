package com.ba.halloweenmovies.ui.screens.filmlist

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.halloweenmovies.data.repository.FetchError
import com.ba.halloweenmovies.data.repository.Films
import com.ba.halloweenmovies.data.repository.FilmsRequestResult
import com.ba.halloweenmovies.ui.CurrentState
import com.ba.halloweenmovies.ui.ErrorStatus
import com.ba.halloweenmovies.ui.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FilmListViewModel : ViewModel() {
    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    private val films: Films = Films()

    init {
        fetchFilms()
    }

    fun isFilmFavourite(filmId: Int): Boolean = films.isFavourite(filmId)

    fun toggleFavourite(filmId: Int) {
        films.toggleFavourite(filmId)
    }

    fun markErrorAsSeen(fetchError: FetchError) {
        _screenState.update { oldValue ->
            oldValue.copy(
                errorStatus = ErrorStatus(
                    seen = true,
                    fetchError
                )
            )
        }
    }

    fun fetchFilms(refresh: Boolean = false) {
        viewModelScope.launch {
            val currentState = _screenState.value
            if (currentState.currentState == CurrentState.Loading ||
                currentState.errorStatus.fetchError == FetchError.NoDataLeftError)
                return@launch

            _screenState.update { oldValue ->
                oldValue.copy(
                    currentState = CurrentState.Loading
                )
            }

            val newFilms = if (refresh) {
                _screenState.update { oldValue ->
                    oldValue.copy(
                        items = emptyList(),
                        currentState = CurrentState.Refreshing
                    )
                }
                films.getInitFilms()
            } else
                films.getNextFilms()

            when (newFilms) {
                is FilmsRequestResult.Success -> {
                    _screenState.update { oldValue ->
                        oldValue.copy(
                            items = oldValue.items + newFilms.gifs,
                            errorStatus = ErrorStatus(false, FetchError.Ok)
                        )
                    }
                }

                is FilmsRequestResult.Error -> {
                    _screenState.update { oldValue ->
                        oldValue.copy(
                            errorStatus = ErrorStatus(false, newFilms.fetchError)
                        )
                    }
                }

                is FilmsRequestResult.Empty -> {
                    _screenState.update { oldValue ->
                        oldValue.copy(
                            errorStatus = ErrorStatus(true, FetchError.NoDataLeftError)
                        )
                    }
                }
            }

            _screenState.update { oldValue ->
                oldValue.copy(
                    currentState = CurrentState.Still
                )
            }
        }
    }
}
