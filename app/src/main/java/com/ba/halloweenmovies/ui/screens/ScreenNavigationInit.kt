package com.ba.halloweenmovies.ui.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ba.halloweenmovies.R
import com.ba.halloweenmovies.data.repository.FilmsRepository
import com.ba.halloweenmovies.ui.screens.favourites.FavouritesScreen
import com.ba.halloweenmovies.ui.screens.filmlist.PopularFilmListScreen
import com.ba.halloweenmovies.ui.screens.profile.ProfileScreen
import com.ba.halloweenmovies.ui.screens.search.SearchScreen
import com.ba.halloweenmovies.ui.screens.search.SearchScreenResults


@Composable
fun MainScreenView(films: FilmsRepository) {
    val navController = rememberNavController()

    val currentScreen by navController.currentBackStackEntryAsState()
    val screen = when (currentScreen?.destination?.route) {
        Screen.FilmList.route -> Screen.FilmList
        Screen.Favourites.route -> Screen.Favourites
        Screen.Search.route -> Screen.Search
        Screen.Profile.route -> Screen.Profile
        Screen.SearchResult.route -> Screen.SearchResult
        else -> Screen.FilmList
    }

    NavigationGraph(navController = navController, screen, films)
}


object AppRoutes {
    const val POPULAR = "popular"
    const val FAVORITES = "favourites"
    const val SEARCH = "search"
    const val SEARCH_RESULT = "searchResults"
    const val PROFILE = "profile"
}

sealed class Screen(val route: String, val titleId: Int, val iconId: Int) {
    data object FilmList : Screen(
        AppRoutes.POPULAR, R.string.films, R.drawable.home_ic
    )

    data object Favourites : Screen(
        AppRoutes.FAVORITES, R.string.favourites, R.drawable.bookmark_ic
    )

    data object Profile : Screen(
        AppRoutes.PROFILE, R.string.profile, R.drawable.ghost_ic
    )

    data object Search : Screen(
        AppRoutes.SEARCH, R.string.search, R.drawable.search_ic
    )

    data object SearchResult : Screen(
        AppRoutes.SEARCH_RESULT, R.string.search, R.drawable.search_ic
    ) {
        fun createRoute(query: String) = "${AppRoutes.SEARCH_RESULT}/$query"
    }
}

fun Screen.getTitle(context: Context): String = when (this) {
    Screen.FilmList -> context.getString(Screen.FilmList.titleId)
    Screen.Favourites -> context.getString(Screen.Favourites.titleId)
    Screen.Profile -> context.getString(Screen.Profile.titleId)
    Screen.Search -> context.getString(Screen.Search.titleId)
    Screen.SearchResult -> context.getString(Screen.SearchResult.titleId)
}


@Composable
fun NavigationGraph(navController: NavHostController, screen: Screen, films: FilmsRepository) {
    val context = LocalContext.current

    NavHost(navController, startDestination = Screen.FilmList.route) {
        composable(Screen.FilmList.route) { PopularFilmListScreen(screen, navController, films) }
        composable(Screen.Favourites.route) { FavouritesScreen(screen, navController, films) }
        composable(Screen.Search.route) { SearchScreen(screen, navController) }
        composable(Screen.Profile.route) { ProfileScreen(screen, navController) }
        composable(
            Screen.SearchResult.createRoute("{${context.getString(R.string.query_argument)}}"),
            arguments = listOf(navArgument(context.getString(R.string.query_argument)) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val query =
                backStackEntry.arguments?.getString(context.getString(R.string.query_argument))
            query?.let { SearchScreenResults(Screen.SearchResult, navController, films, it) }
        }
    }
}
