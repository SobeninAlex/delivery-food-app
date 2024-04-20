package com.example.delivery_food_app.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import com.example.delivery_food_app.presentation.ui.theme.ButtonsCounterColor

@Composable
fun Counter(
    modifier: Modifier = Modifier,
    count: String,
    onClickPlus: () -> Unit,
    onClickMinus: () -> Unit,
) {
    Row(modifier = modifier) {
        IconButton(
            modifier = Modifier
                .background(
                    color = ButtonsCounterColor,
                    shape = RoundedCornerShape(8.dp)
                ),
            onClick = { onClickMinus() }
        ) {
            Icon(
                Icons.Default.Remove, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Box(
            modifier = Modifier.size(47.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = count,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(
            modifier = Modifier
                .background(
                    color = ButtonsCounterColor,
                    shape = RoundedCornerShape(8.dp)
                ),
            onClick = { onClickPlus() }
        ) {
            Icon(
                Icons.Default.Add, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}