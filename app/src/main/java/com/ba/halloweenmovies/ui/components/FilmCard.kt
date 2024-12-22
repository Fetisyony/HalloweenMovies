package com.ba.halloweenmovies.ui.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.SubcomposeAsyncImage
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.ba.halloweenmovies.R
import com.ba.halloweenmovies.data.model.Film


@Composable
fun FilmCard(film: Film) {
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

    SubcomposeAsyncImage(
        model = model,
        imageLoader = imageLoader,
        contentDescription = stringResource(R.string.image_description_main_list),
        modifier = Modifier
            .fillMaxWidth(),
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
                        .size(80.dp)
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
}