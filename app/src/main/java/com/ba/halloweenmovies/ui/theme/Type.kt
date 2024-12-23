package com.ba.halloweenmovies.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ba.halloweenmovies.R


val filmTitleStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.inter_regular), Font(R.font.inter_italic)),
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    color = Color.Black
)

val filmYearStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.inter_regular), Font(R.font.inter_italic)),
    fontWeight = FontWeight.Normal,
    fontSize = 15.sp,
    color = FilmYearText
)

val filmRatingStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.inter_regular), Font(R.font.inter_italic)),
    fontWeight = FontWeight.Bold,
    fontSize = 14.sp,
    color = Color.White
)



// Set of Material typography styles to start with
val typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)