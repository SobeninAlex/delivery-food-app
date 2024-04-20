package com.example.delivery_food_app.presentation.basket

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delivery_food_app.R
import com.example.delivery_food_app.domain.entity.Product
import com.example.delivery_food_app.domain.entity.ProductItem
import com.example.delivery_food_app.presentation.ui.component.Counter
import com.example.delivery_food_app.presentation.ui.component.LineThroughText


@Composable
fun BasketContent(
    modifier: Modifier = Modifier,
    component: BasketComponent
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

        when (val current = state.productState) {
            is BasketStore.State.ProductState.EmptyResult -> {
                EmptyBasket(modifier = modifier.padding(paddingValues))
            }

            is BasketStore.State.ProductState.Initial -> {
                Box(modifier = modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = modifier
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            is BasketStore.State.ProductState.Loaded -> {
                Content(
                    products = current.products,
                    paddingValues = paddingValues,
                    onClickAddToBasket = {
                        component.onClickAddToBasket(it)
                    },
                    onClickRemovedFromBasket = {
                        component.onClickRemoveFromBasket(it)
                    }
                )
            }
        }
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

@Composable
fun Content(
    modifier: Modifier = Modifier,
    products: List<ProductItem>,
    paddingValues: PaddingValues,
    onClickRemovedFromBasket: (Product) -> Unit,
    onClickAddToBasket: (Product) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(
                items = products,
                key = {
                    it.id
                }
            ) { productItem ->
                ProductItem(
                    product = productItem.product,
                    count = productItem.count.toString(),
                    onClickRemovedFromBasket = {
                        onClickRemovedFromBasket(productItem.product)
                    },
                    onClickAddToBasket = {
                        onClickAddToBasket(productItem.product)
                    }
                )
            }
        }

        Box(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(72.dp)
                .background(color = MaterialTheme.colorScheme.background)
                .shadow(elevation = 1.dp)
                .padding(vertical = 12.dp, horizontal = 16.dp),
        ) {
            Button(
                modifier = modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(size = 8.dp),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = stringResource(R.string.order, ""),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    count: String,
    onClickRemovedFromBasket: () -> Unit,
    onClickAddToBasket: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = modifier
                    .wrapContentSize()
                    .padding(16.dp)
            ) {
                Image(
                    modifier = modifier.width(96.dp),
                    painter = painterResource(id = R.drawable.prod_item),
                    contentDescription = null
                )
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(
                    space = 24.dp,
                    Alignment.CenterVertically
                )
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp
                )


                Row(modifier = modifier) {
                    Counter(
                        modifier = modifier.weight(1f),
                        count = count,
                        onClickMinus = { onClickRemovedFromBasket() },
                        onClickPlus = { onClickAddToBasket() }
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${product.priceCurrent} ₽",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (product.priceOld != null) {
                            LineThroughText(text = product.priceOld)
                        }
                    }

                }
            }


        }
    }

//    HorizontalDivider(thickness = 2.dp, color = DividerDefaults.color.copy(alpha = 0.5f))
}

@Composable
private fun EmptyBasket(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            modifier = modifier.align(Alignment.Center),
            text = "Корзина пуста",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 18.sp
            )
        )
    }
}