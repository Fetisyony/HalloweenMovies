package com.ba.halloweenmovies.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ba.halloweenmovies.ui.screens.Screen
import com.ba.halloweenmovies.ui.screens.getTitle
import com.ba.halloweenmovies.ui.theme.SecondaryGrayColor
import com.ba.halloweenmovies.ui.theme.SelectedSecondaryColor


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.FilmList, Screen.Favourites, Screen.Search, Screen.Profile
    )

    BottomNavigation(
        elevation = 0.dp,
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxHeight()
            .padding(0.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            val isSelected = currentRoute == screen.route

            BottomNavigationItem(
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = ImageVector.vectorResource(id = screen.iconId),
                        tint = if (isSelected) SelectedSecondaryColor else SecondaryGrayColor,
                        contentDescription = screen.getTitle(LocalContext.current),
                    )
                },
                label = {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = screen.getTitle(LocalContext.current),
                        fontSize = 12.sp,
                        color = if (isSelected) SelectedSecondaryColor else SecondaryGrayColor
                    )
                },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = Color.Blue
            )
        }
    }
}

