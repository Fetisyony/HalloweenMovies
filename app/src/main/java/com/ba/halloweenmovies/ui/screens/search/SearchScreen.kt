package com.ba.halloweenmovies.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ba.halloweenmovies.R
import com.ba.halloweenmovies.ui.screens.MainScreensBase
import com.ba.halloweenmovies.ui.screens.Screen

@Composable
fun SearchScreen(screen: Screen, navController: NavHostController) {
    MainScreensBase(
        screen = screen, navController = navController
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            SearchEntryScreen(navController)
        }
    }
}

@Composable
fun SearchEntryScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        shape = RoundedCornerShape(32.dp),
        trailingIcon = {
            Icon(painter = painterResource(id = R.drawable.search_ic),
                contentDescription = stringResource(R.string.search_icon),
                modifier = Modifier
                    .size(34.dp)
                    .clickable {
                        if (searchQuery.text.isNotEmpty()) navController.navigate(
                            Screen.SearchResult.createRoute(
                                searchQuery.text
                            )
                        )
                    })
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(44.dp),
        textStyle = TextStyle(
            fontSize = 16.sp
        ),
    )
}
