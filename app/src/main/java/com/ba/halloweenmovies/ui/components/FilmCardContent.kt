package com.ba.halloweenmovies.ui.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.SubcomposeAsyncImage
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.ba.halloweenmovies.R
import com.ba.halloweenmovies.data.model.Film
import com.ba.halloweenmovies.ui.theme.GrayRateColor
import com.ba.halloweenmovies.ui.theme.GreenRateColor
import com.ba.halloweenmovies.ui.theme.PumpkinColor
import com.ba.halloweenmovies.ui.theme.RedRateColor
import com.ba.halloweenmovies.ui.theme.filmRatingStyle
import com.ba.halloweenmovies.ui.theme.filmTitleStyle
import com.ba.halloweenmovies.ui.theme.filmYearStyle
import kotlinx.coroutines.launch


fun getRatingColor(rating: Float): Color {
    return when {
        rating < 4.0 -> RedRateColor
        rating < 7.0 -> GrayRateColor
        else -> GreenRateColor
    }
}

@Composable
fun FilmCardContent(film: Film, onAddToFavourites: (Int) -> Unit) {
    val context = LocalContext.current

    val decoderFactory = if (SDK_INT >= 28) AnimatedImageDecoder.Factory() else GifDecoder.Factory()
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(decoderFactory)
            }
            .build()
    }
    val model = remember(film.posterUrl) {
        ImageRequest.Builder(context)
            .data(film.posterUrl)
            .crossfade(true)
            .build()
    }

    var isFilmFavourite by remember { mutableStateOf(film.isFavourite) }

    Column {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            SubcomposeAsyncImage(
                model = model,
                imageLoader = imageLoader,
                contentDescription = stringResource(R.string.image_description_main_list),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(248.dp),
                contentScale = ContentScale.Crop,
                error = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.round_cloud_off_24),
                            contentDescription = stringResource(id = R.string.error_loading_image_cd),
                            modifier = Modifier
                                .height(248.dp)
                        )
                    }
                },
                loading = {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .size(15.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            strokeWidth = 5.dp,
                        )
                    }
                }
            )
            if (!isFilmFavourite) {
                AddToFavouritesIcon(
                    onClick = {
                        onAddToFavourites(film.id)
                        isFilmFavourite = true
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            } else {
                RemoveFromFavouritesIcon(
                    onClick = {
                        onAddToFavourites(film.id)
                        isFilmFavourite = false
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
            RatingBadge(film.rating, modifier = Modifier.align(Alignment.BottomEnd))
        }
        Box(
            modifier = Modifier
                .padding(12.dp)
                .height(70.dp)
        ) {
            Column {
                Text(
                    text = film.title,
                    style = filmTitleStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = film.year.toString(),
                    style = filmYearStyle
                )
            }
        }
    }
}

@Composable
fun RatingBadge(
    rating: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .width(53.dp)
            .height(26.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(getRatingColor(rating)),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(getRatingColor(rating)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = stringResource(R.string.film_rating_description),
                tint = Color.White,
                modifier = Modifier
                    .background(getRatingColor(rating))
                    .padding(5.dp)
            )
            Text(
                text = rating.toString(),
                style = filmRatingStyle,
            )
        }
    }
}

@Composable
fun AddToFavouritesIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    Card(
        modifier = modifier
            .padding(4.dp)
            .size(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(PumpkinColor)
            .clickable {
                coroutineScope.launch {
                    onClick()
                }
            },
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = R.drawable.bookmark_add),
                contentDescription = stringResource(R.string.add_bookmark_description),
                tint = Color.Black,
                modifier = Modifier
                    .fillMaxSize()
                    .background(PumpkinColor)
                    .padding(5.dp)
            )
        }
    }
}


@Composable
fun RemoveFromFavouritesIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .size(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(PumpkinColor)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = R.drawable.outline_bookmark_remove_24),
                contentDescription = stringResource(R.string.add_bookmark_description),
                tint = Color.Black,
                modifier = Modifier
                    .fillMaxSize()
                    .background(PumpkinColor)
                    .padding(5.dp)
            )
        }
    }
}
