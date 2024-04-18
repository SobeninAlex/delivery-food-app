package com.example.delivery_food_app.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.delivery_food_app.R

val Typography = Typography(
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Color.Black,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)),
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = Color.Black
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)),
        fontWeight = FontWeight.Thin,
        fontSize = 16.sp,
        color = Color.Black.copy(alpha = 0.6f),
    ),
)