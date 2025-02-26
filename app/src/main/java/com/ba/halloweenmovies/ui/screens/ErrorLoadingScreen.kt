package com.ba.halloweenmovies.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ba.halloweenmovies.R
import com.ba.halloweenmovies.ui.components.RetryButton
import com.ba.halloweenmovies.ui.theme.ErrorScreenColor

@Composable
fun ErrorLoadingScreen(
    errorMessage: String,
    onTryAgain: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.round_cloud_off_24),
            contentDescription = stringResource(R.string.error_loading_image_cd)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = errorMessage,
            textAlign = TextAlign.Center,
            color = ErrorScreenColor
        )
        Spacer(modifier = Modifier.height(21.dp))
        RetryButton(onTryAgain)
    }
}
