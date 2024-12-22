package com.ba.halloweenmovies.ui

import androidx.compose.runtime.Immutable
import com.ba.halloweenmovies.data.model.Film
import com.ba.halloweenmovies.data.repository.FetchError


data class ErrorStatus(
    val seen: Boolean,  // whether an alert was already shown to the user
    val fetchError: FetchError
)

sealed interface CurrentState {
    data object Loading : CurrentState
    data object Refreshing : CurrentState
    data object Still: CurrentState
}

@Immutable
data class ScreenState(
    val items: List<Film> = emptyList(),
    val currentState: CurrentState = CurrentState.Still,
    val errorStatus: ErrorStatus = ErrorStatus(true, FetchError.Ok)
)
