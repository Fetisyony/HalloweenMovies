package com.ba.halloweenmovies.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ba.halloweenmovies.data.repository.FilmsRepository
import com.ba.halloweenmovies.ui.screens.FilmListScreen
import com.ba.halloweenmovies.ui.screens.MainScreensBase
import com.ba.halloweenmovies.ui.screens.Screen


class SearchedListViewModelFactory(
    private val films: FilmsRepository, private val requestString: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchResultsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return SearchResultsViewModel(films, requestString) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun SearchScreenResults(
    screen: Screen, navController: NavHostController, films: FilmsRepository, requestString: String
) {
    val viewModel: SearchResultsViewModel = viewModel(
        factory = SearchedListViewModelFactory(films, requestString)
    )
    val screenState = viewModel.screenState.collectAsState()

    MainScreensBase(
        screen = screen, navController = navController, buttonBack = true
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            FilmListScreen(viewModel, screenState, false)
        }
    }
}
