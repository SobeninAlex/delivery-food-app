package com.example.delivery_food_app.presentation.catalog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.State
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
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
                Box(modifier = Modifier.fillMaxSize()) {
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

                    val visible = current.catalog.any { it.count > 0 }
                    val totalPrice = current.catalog.sumOf { it.price }

                    if (visible) {
                        AnimatedButton(
                            price = totalPrice,
                            modifier = Modifier.align(Alignment.BottomCenter),
                            moveToBasket = {
                                component.onClickBasketIcon()
                            }
                        )
                    }
                }
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
                    imageVector = Icons.Default.LocalGroceryStore,
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

@Composable
private fun AnimatedButton(
    modifier: Modifier = Modifier,
    price: Int,
    moveToBasket: () -> Unit
) {

    val visibleState = remember {
        MutableTransitionState(initialState = false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visibleState = visibleState,
        enter = fadeIn(animationSpec = tween(500)) + slideIn(
            animationSpec = tween(500),
            initialOffset = {
                IntOffset(x = 0, y = it.height)
            }
        ),
        exit = fadeOut(animationSpec = tween(500)) + slideOut(
            animationSpec = tween(500),
            targetOffset = { IntOffset(x = 0, y = 0) }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(color = Color.Transparent)
                .padding(vertical = 12.dp, horizontal = 16.dp),
        ) {
            Button(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(size = 8.dp),
                onClick = { moveToBasket() }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalGroceryStore,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text(
                        text = "$price ₽",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }


            }
        }
    }
}












