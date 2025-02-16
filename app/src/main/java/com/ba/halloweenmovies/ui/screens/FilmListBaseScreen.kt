package com.ba.halloweenmovies.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import com.ba.halloweenmovies.data.model.Film
import com.ba.halloweenmovies.data.repository.FetchError
import com.ba.halloweenmovies.ui.CurrentState
import com.ba.halloweenmovies.ui.ScreenState
import com.ba.halloweenmovies.ui.components.CustomAlertDialog
import com.ba.halloweenmovies.ui.components.FilmGrid
import com.ba.halloweenmovies.ui.screens.filmlist.getErrorMessage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListScreen(
    viewModel: BaseFilmListViewModel<Film>,
    screenState: State<ScreenState>,
    showErrors: Boolean = true
) {
    val refreshState = rememberPullToRefreshState()
    val context = LocalContext.current

    PullToRefreshBox(
        isRefreshing = screenState.value.currentState == CurrentState.Refreshing,
        state = refreshState,
        onRefresh = {
            viewModel.fetchItems(refresh = true)
        }
    ) {
        FilmGrid(
            screenState = screenState.value,
            onLoadMore = {
                viewModel.fetchItems()
            },
            { id -> viewModel.toggleFavourite(id) },
        )

        if (screenState.value.errorStatus.fetchError != FetchError.Ok) {
            if (screenState.value.items.isEmpty() && screenState.value.currentState == CurrentState.Still) {
                ErrorLoadingScreen(
                    screenState.value.errorStatus.fetchError.getErrorMessage(context),
                ) {
                    viewModel.fetchItems(refresh = true)
                }
            } else if (!screenState.value.errorStatus.seen && showErrors) {
                CustomAlertDialog(
                    message = screenState.value.errorStatus.fetchError.getErrorMessage(context),
                    onConfirm = { viewModel.markErrorAsSeen(screenState.value.errorStatus.fetchError) },
                )
            }
        }
    }
}
