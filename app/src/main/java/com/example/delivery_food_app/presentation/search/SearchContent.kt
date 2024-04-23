package com.example.delivery_food_app.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.delivery_food_app.R
import com.example.delivery_food_app.presentation.ui.component.CatalogProducts
import com.example.delivery_food_app.presentation.ui.component.Loader
import com.example.delivery_food_app.presentation.ui.component.SomeWrong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    component: SearchComponent
) {
    val state by component.model.collectAsState()

    SearchBar(
        query = state.searchQuery,
        onQueryChange = {
            component.changeSearchQuery(it)
        },
        onSearch = {
            component.onClickSearch()
        },
        active = true,
        onActiveChange = {},
        leadingIcon = {
            IconButton(
                onClick = { component.onClickBack() },
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = { component.onClickSearch() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        placeholder = {
            Text(
                text = stringResource(R.string.name_prod),
                style = MaterialTheme.typography.bodySmall
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        when (val current = state.searchState) {
            is SearchStore.State.SearchState.EmptyResult -> {
                EmptyResult()
            }

            is SearchStore.State.SearchState.Error -> {
                SomeWrong()
            }

            is SearchStore.State.SearchState.Initial -> {
                SearchInitial()
            }

            is SearchStore.State.SearchState.Loading -> {
                Loader()
            }

            is SearchStore.State.SearchState.SuccessLoaded -> {
                CatalogProducts(
                    products = current.products,
                    onClickCard = {
                        component.onClickProduct(it)
                    },
                    onClickAddToBasket = {
                        component.onClickAddToBasket(it)
                    },
                    onClickRemoveFromBasket = {
                        component.onClickRemoveFromBasket(it)
                    }
                )
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