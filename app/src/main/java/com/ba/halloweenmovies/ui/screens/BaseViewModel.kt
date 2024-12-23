package com.ba.halloweenmovies.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.halloweenmovies.data.repository.FetchError
import com.ba.halloweenmovies.data.repository.FilmsRepository
import com.ba.halloweenmovies.data.repository.FilmsRequestResult
import com.ba.halloweenmovies.ui.CurrentState
import com.ba.halloweenmovies.ui.ErrorStatus
import com.ba.halloweenmovies.ui.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


abstract class BaseFilmListViewModel<T>(
    protected val films: FilmsRepository,
    private val requestString: String? = null
) : ViewModel() {
    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    init {
        fetchItems(true)
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

    protected abstract suspend fun fetchItemsCore(
        refresh: Boolean = false,
        requestString: String? = null
    ): FilmsRequestResult

    fun fetchItems(refresh: Boolean = false) {
        viewModelScope.launch {
            if (_screenState.value.currentState != CurrentState.Still)
                return@launch

            _screenState.update { oldValue ->
                oldValue.copy(
                    items = if (refresh) emptyList() else oldValue.items,
                    currentState = if (refresh) CurrentState.Refreshing else CurrentState.Loading
                )
            }

            var updatedItems = _screenState.value.items
            var updatedErrorStatus = _screenState.value.errorStatus

            when (val newFilms = fetchItemsCore(refresh, requestString)) {
                is FilmsRequestResult.Success -> {
                    updatedItems = updatedItems + newFilms.films
                    updatedErrorStatus = ErrorStatus(false, FetchError.Ok)
                }

                is FilmsRequestResult.Error -> {
                    updatedErrorStatus = ErrorStatus(
                        updatedErrorStatus.fetchError == newFilms.fetchError,
                        newFilms.fetchError
                    )
                }

                is FilmsRequestResult.Empty -> {
                    updatedErrorStatus = ErrorStatus(
                        updatedErrorStatus.fetchError == FetchError.NoDataLeftError,
                        FetchError.NoDataLeftError
                    )
                }
            }

            _screenState.update { oldValue ->
                oldValue.copy(
                    items = updatedItems,
                    errorStatus = updatedErrorStatus,
                    currentState = CurrentState.Still
                )
            }
        }
    }

    fun isFavourite(filmId: Int): Boolean = films.isFavourite(filmId)

    fun toggleFavourite(filmId: Int) {
        films.toggleFavourite(filmId)
    }
}