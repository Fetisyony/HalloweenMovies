package com.ba.halloweenmovies.ui.screens

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsIgnoringVisibility
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ba.halloweenmovies.R
import com.ba.halloweenmovies.ui.components.BottomNavigationBar
import com.ba.halloweenmovies.ui.theme.MainBackgroundColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MainScreensBase(
    screen: Screen,
    navController: androidx.navigation.NavHostController,
    buttonBack: Boolean = false,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.systemBarsIgnoringVisibility,
        containerColor = MainBackgroundColor,
        topBar = {
            TopAppBar(
                modifier = Modifier.height(90.dp),
                title = {
                    Row(
                        modifier = Modifier.padding(horizontal = 18.dp)
                    ) {
                        val painter = if (buttonBack)
                                painterResource(R.drawable.arrow_back_ic)
                            else
                                painterResource(R.drawable.pumpkin_logo)
                        val description = if (buttonBack)
                                stringResource(R.string.back_icon)
                            else
                                stringResource(R.string.pumpkin_logo)
                        IconButton(
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp),
                            onClick = {
                                if (buttonBack)
                                    navController.popBackStack()
                            }
                        ) {
                            Icon(
                                painter,
                                description,
                                tint = Color.Unspecified
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = screen.getTitle(LocalContext.current))
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                contentPadding = PaddingValues(0.dp),
                windowInsets = WindowInsets.waterfall
            ) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}
