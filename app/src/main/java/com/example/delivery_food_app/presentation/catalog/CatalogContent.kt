package com.example.delivery_food_app.presentation.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.delivery_food_app.R
import com.example.delivery_food_app.presentation.ui.component.CatalogProducts
import com.example.delivery_food_app.presentation.ui.component.Loader

@Composable
fun CatalogContent(
    modifier: Modifier = Modifier,
    component: CatalogComponent
) {
    val state by component.model.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                onClickBasketIcon = {
                    component.onClickBasketIcon()
                },
                onClickSearchIcon = {
                    component.onClickSearchIcon()
                }
            )
        }
    ) { paddingValues ->
        when (val current = state.catalogStatus) {
            is CatalogStore.State.CatalogStatus.Error -> {}

            is CatalogStore.State.CatalogStatus.Initial -> {}

            is CatalogStore.State.CatalogStatus.Loaded -> {
                CatalogProducts(
                    products = current.catalog,
                    paddingValues = paddingValues,
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

            is CatalogStore.State.CatalogStatus.Loading -> {
                Loader()
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    onClickBasketIcon: () -> Unit,
    onClickSearchIcon: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Image(
                modifier = modifier.width(110.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            IconButton(onClick = { onClickBasketIcon() }) {
                Icon(
                    imageVector = Icons.Default.ShoppingBasket,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            IconButton(onClick = { onClickSearchIcon() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    )
}












