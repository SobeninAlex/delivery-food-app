package com.example.delivery_food_app.presentation.catalog

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delivery_food_app.R
import com.example.delivery_food_app.presentation.ui.component.LineThroughText
import com.example.delivery_food_app.presentation.ui.theme.ContainerColor

@Composable
fun CatalogContent(
    modifier: Modifier = Modifier,
    component: CatalogComponent
) {

    val state by component.model.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                onClickBasketIcon = { component.onClickBasketIcon() }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = state.productItems,
                key = { it.product.id }
            ) { productItem ->
                ProductCard(
                    productItem = productItem,
                    onClickCard = {
                        component.onClickProduct(productItem.product)
                    },
                    onClickAddToBasket = {
                        component.onClickAddToBasket(productItem.product)
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
    onClickBasketIcon: () -> Unit
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
        }
    )
}

@Composable
private fun ProductCard(
    modifier: Modifier = Modifier,
    productItem: CatalogStore.State.ProductItem,
    onClickCard: () -> Unit,
    onClickAddToBasket: () -> Unit
) {

    when (val state = productItem.productStatus) {
        is CatalogStore.State.ProductStatus.Error -> {}
        is CatalogStore.State.ProductStatus.Initial -> {}

        is CatalogStore.State.ProductStatus.Loaded -> {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .height(290.dp),
                colors = CardDefaults.cardColors(
                    containerColor = ContainerColor,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(size = 8.dp),
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize(),
                ) {
                    Box(
                        modifier = modifier.clickable { onClickCard() },
                    ) {
                        Image(
                            modifier = modifier
                                .width(167.dp),
                            painter = painterResource(id = R.drawable.prod_item),
                            contentDescription = null
                        )
                    }
                    Column(
                        modifier = modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            maxLines = 1,
                            text = state.product.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 14.sp
                        )

                        Text(
                            text = state.product.weight,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 14.sp
                        )

                        Box (modifier = modifier.fillMaxSize()) {
                            Button(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .align(Alignment.BottomCenter),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                ),
                                contentPadding = PaddingValues(horizontal = 4.dp),
                                onClick = { onClickAddToBasket() },
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Text(
                                        text = "${state.product.priceCurrent} ₽",
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    if (state.product.priceOld != null) {
                                        LineThroughText(text = state.product.priceOld)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        is CatalogStore.State.ProductStatus.Loading -> {
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = modifier
                        .align(Alignment.Center),
                    color = Color.Black
                )
            }
        }
    }

}











