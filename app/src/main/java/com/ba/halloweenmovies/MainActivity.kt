package com.ba.halloweenmovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ba.halloweenmovies.data.repository.FilmsRepository
import com.ba.halloweenmovies.data.room.AppDatabase
import com.ba.halloweenmovies.ui.screens.MainScreenView
import com.ba.halloweenmovies.ui.theme.HalloweenMoviesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val films = FilmsRepository(database.filmDao())

//        val database by lazy { AppDatabase.getDatabase(this) }
//        val repository by lazy { FilmsRepository(database.filmDao()) }

        enableEdgeToEdge()
        setContent {
            HalloweenMoviesTheme {
                MainScreenView(films)
            }
        }
    }
}
