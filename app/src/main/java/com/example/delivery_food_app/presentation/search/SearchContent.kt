package com.example.delivery_food_app.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delivery_food_app.R
import com.example.delivery_food_app.presentation.ui.component.Loader

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    component: SearchComponent
) {
    val state by component.model.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            TopBar(
                onClickBack = {
                    component.onClickBack()
                }
            )
        }
    ) { paddingValues ->

        when (val current = state.searchState) {
            is SearchStore.State.SearchState.EmptyResult -> {
                EmptyResult(modifier = modifier.padding(paddingValues))
            }

            is SearchStore.State.SearchState.Error -> {}

            is SearchStore.State.SearchState.Initial -> {
                SearchInitial()
            }

            is SearchStore.State.SearchState.Loading -> {
                Loader()
            }

            is SearchStore.State.SearchState.SuccessLoaded -> {

            }
        }
    }
}

@Composable
private fun EmptyResult(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.empty_search),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 18.sp
            )
        )
    }
}

@Composable
private fun SearchInitial(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.enter_name_food),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 18.sp
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
) {
    TopAppBar(
        modifier = modifier
            .shadow(elevation = 2.dp),
        title = {
            Text(
                modifier = modifier.padding(start = 14.dp),
                text = stringResource(R.string.basket_title),
                style = MaterialTheme.typography.titleMedium,
            )
        },
        navigationIcon = {
            IconButton(
                modifier = modifier.background(color = Color.Transparent),
                onClick = { onClickBack() },
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}