package com.example.delivery_food_app.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Counter(
    modifier: Modifier = Modifier,
    buttonsColor: Color,
    size: Dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    count: String,
    onClickPlus: () -> Unit,
    onClickMinus: () -> Unit,
) {
    Row(modifier = modifier, horizontalArrangement = horizontalArrangement) {
        IconButton(
            modifier = Modifier.background(
                color = buttonsColor,
                shape = RoundedCornerShape(8.dp)
            ).size(size),
            onClick = { onClickMinus() }
        ) {
            Icon(
                Icons.Default.Remove, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Box(
            modifier = Modifier.size(size),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = count,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(
            modifier = Modifier.background(
                color = buttonsColor,
                shape = RoundedCornerShape(8.dp)
            ).size(size),
            onClick = { onClickPlus() }
        ) {
            Icon(
                Icons.Default.Add, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}