package com.example.delivery_food_app.presentation.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle


@Composable
fun LineThroughText(text: String, textStyle: TextStyle) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                append(text)
            }
        },
        style = textStyle
    )
}