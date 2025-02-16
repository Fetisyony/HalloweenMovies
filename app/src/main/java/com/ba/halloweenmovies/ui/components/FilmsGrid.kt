package com.ba.halloweenmovies.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ba.halloweenmovies.data.repository.FetchError
import com.ba.halloweenmovies.ui.CurrentState
import com.ba.halloweenmovies.ui.ScreenState


fun isHighTimeToLoadNew(lastVisibleItemIndex: Int?, screenState: ScreenState): Boolean {
    val loadNewWhenLeftUntilBottom = 5

    return lastVisibleItemIndex != null &&
            screenState.currentState == CurrentState.Still &&
            lastVisibleItemIndex >= screenState.items.size - loadNewWhenLeftUntilBottom &&
            screenState.errorStatus.fetchError == FetchError.Ok
}

@Composable
fun FilmGrid(
    screenState: ScreenState,
    onLoadMore: () -> Unit,
    onToggleFavourites: (Int) -> Unit,
) {
    val listState = rememberSaveable(saver = LazyGridState.Saver) {
        LazyGridState()
    }

    LaunchedEffect(listState, screenState.currentState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val lastVisibleItemIndex = visibleItems.lastOrNull()?.index
                if (isHighTimeToLoadNew(lastVisibleItemIndex, screenState)) {
                    onLoadMore()
                }
            }
    }

    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 4.dp)
            .padding(horizontal = 16.dp),
    ) {
        items(screenState.items) { film ->
            ElevatedCard(
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.elevatedCardElevation(0.dp),
                colors = CardColors(
                    containerColor = Color.White,
                    contentColor = Color.White,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.White,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(322.dp)
            ) {
                FilmCardContent(film, onToggleFavourites)
            }
        }

        if (screenState.currentState == CurrentState.Loading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(top = 0.dp, bottom = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }
        }

        if (screenState.errorStatus.fetchError != FetchError.Ok && screenState.items.isNotEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    RetryButton(onLoadMore)
                }
            }
        }
    }
}
