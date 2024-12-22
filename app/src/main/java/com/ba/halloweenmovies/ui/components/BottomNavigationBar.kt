package com.ba.halloweenmovies.ui.components
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ba.halloweenmovies.ui.screens.favourites.FavouritesScreen
import com.ba.halloweenmovies.ui.screens.filmlist.PopularFilmListScreen
import com.ba.halloweenmovies.ui.screens.profile.ProfileScreen


sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object FilmList : Screen("film_list", "Films", Icons.Filled.Movie)
    object Favourites : Screen("favourites", "Favourites", Icons.Filled.Favorite)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)
}



@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.FilmList,
        Screen.Favourites,
        Screen.Profile
    )

    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Restore state when reselecting a previously selected item
                        launchSingleTop = true
                        // Restore state even if the route is the current destination
                        restoreState = true
                    }
                }

            )
        }
    }
}



@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.FilmList.route) {
        composable(Screen.FilmList.route) { PopularFilmListScreen( /*...pass your viewmodel here */) }
        composable(Screen.Favourites.route) { FavouritesScreen(/*...pass your viewmodel here */) }
        composable(Screen.Profile.route) { ProfileScreen(/*...pass your viewmodel here */) }
    }
}


@Composable
fun MainScreenView() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding -> // Apply insets to the content area
        NavigationGraph(navController = navController)
    }
}