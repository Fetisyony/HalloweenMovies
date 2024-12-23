package com.ba.halloweenmovies.ui.screens.filmlist

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ba.halloweenmovies.R
import com.ba.halloweenmovies.data.repository.FetchError
import com.ba.halloweenmovies.data.repository.FilmsRepository
import com.ba.halloweenmovies.ui.screens.FilmListScreen
import com.ba.halloweenmovies.ui.screens.MainScreensBase
import com.ba.halloweenmovies.ui.screens.Screen

fun FetchError.getErrorMessage(context: Context): String = when (this) {
    FetchError.Ok -> context.getString(R.string.fetch_error_message_ok)
    FetchError.NetworkError -> context.getString(R.string.fetch_error_message_network)
    FetchError.NoDataLeftError -> context.getString(R.string.fetch_error_message_no_data_left)
    is FetchError.UnexpectedError -> context.getString(R.string.fetch_error_message_unexpected)
}

class FilmListViewModelFactory(private val films: FilmsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PopularFilmsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return PopularFilmsViewModel(films) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun PopularFilmListScreen(
    screen: Screen, navController: NavHostController, films: FilmsRepository
) {
    val viewModel: PopularFilmsViewModel = viewModel(
        factory = FilmListViewModelFactory(films)
    )
    val screenState = viewModel.screenState.collectAsState()

    MainScreensBase(
        screen = screen, navController = navController
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            FilmListScreen(viewModel, screenState, true)
        }
    }
}
