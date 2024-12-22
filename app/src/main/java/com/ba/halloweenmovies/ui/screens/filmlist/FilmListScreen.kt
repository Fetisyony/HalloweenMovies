package com.ba.halloweenmovies.ui.screens.filmlist

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ba.halloweenmovies.R
import com.ba.halloweenmovies.data.repository.FetchError
import com.ba.halloweenmovies.ui.components.FilmGrid
import com.ba.halloweenmovies.ui.CurrentState
import com.ba.halloweenmovies.ui.components.CustomAlertDialog
import com.ba.halloweenmovies.ui.screens.ErrorLoadingScreen

fun FetchError.getErrorMessage(context: Context): String = when (this) {
    FetchError.Ok -> context.getString(R.string.fetch_error_message_ok)
    FetchError.NetworkError -> context.getString(R.string.fetch_error_message_network)
    FetchError.NoDataLeftError -> context.getString(R.string.fetch_error_message_no_data_left)
    is FetchError.UnexpectedError -> context.getString(R.string.fetch_error_message_unexpected)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularFilmListScreen() {
    val viewModel: FilmListViewModel = viewModel()
    val refreshState = rememberPullToRefreshState()
    val screenState = viewModel.screenState.collectAsState()

    val context = LocalContext.current

    PullToRefreshBox(
        isRefreshing = screenState.value.currentState == CurrentState.Refreshing,
        state = refreshState,
        onRefresh = {
            viewModel.fetchFilms(refresh = true)
        }
    ) {
        FilmGrid(
            screenState = screenState.value,
            onLoadMore = {
                viewModel.fetchFilms()
            }
        )

        if (screenState.value.errorStatus.fetchError != FetchError.Ok) {
            if (screenState.value.items.isEmpty()) {
                ErrorLoadingScreen(
                    screenState.value.errorStatus.fetchError.getErrorMessage(context)
                ) {
                    viewModel.fetchFilms(refresh = true)
                }
            } else if (!screenState.value.errorStatus.seen) {
                CustomAlertDialog(
                    message = screenState.value.errorStatus.fetchError.getErrorMessage(context),
                    onConfirm = { viewModel.markErrorAsSeen(screenState.value.errorStatus.fetchError) },
                )
            }
        }
    }
}

