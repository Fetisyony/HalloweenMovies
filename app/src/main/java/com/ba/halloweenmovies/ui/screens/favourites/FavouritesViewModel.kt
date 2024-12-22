package com.ba.halloweenmovies.ui.screens.favourites

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.ba.halloweenmovies.data.model.Film
import com.ba.halloweenmovies.data.repository.Films
import com.ba.halloweenmovies.ui.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavouritesViewModel(private val repository: Films) : ViewModel() {
    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

}
