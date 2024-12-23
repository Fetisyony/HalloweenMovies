package com.ba.halloweenmovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ba.halloweenmovies.data.repository.FilmsRepository
import com.ba.halloweenmovies.ui.screens.MainScreenView
import com.ba.halloweenmovies.ui.theme.HalloweenMoviesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val films = FilmsRepository()

        enableEdgeToEdge()
        setContent {
            HalloweenMoviesTheme {
                MainScreenView(films)
            }
        }
    }
}
