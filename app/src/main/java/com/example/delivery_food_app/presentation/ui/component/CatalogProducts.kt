package com.example.delivery_food_app.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delivery_food_app.R
import com.example.delivery_food_app.domain.entity.ProductItem
import com.example.delivery_food_app.presentation.basket.BasketStore
import com.example.delivery_food_app.presentation.ui.theme.ContainerColor


@Composable
fun CatalogProducts(
    modifier: Modifier = Modifier,
    products: List<ProductItem>,
    paddingValues: PaddingValues = PaddingValues(),
    onClickCard: (ProductItem) -> Unit,
    onClickAddToBasket: (ProductItem) -> Unit,
    onClickRemoveFromBasket: (ProductItem) -> Unit,
) {
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
            items = products,
            key = { it.id }
        ) { productItem ->
            ProductCard(
                productItem = productItem,
                onClickCard = {
                    onClickCard(productItem)
                },
                onClickAddToBasket = {
                    onClickAddToBasket(productItem)
                },
                onClickRemoveFromBasket = {
                    onClickRemoveFromBasket(productItem)
                }
            )
        }
    }
}

@Composable
private fun ProductCard(
    modifier: Modifier = Modifier,
    productItem: ProductItem,
    onClickCard: () -> Unit,
    onClickAddToBasket: () -> Unit,
    onClickRemoveFromBasket: () -> Unit
) {
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
                    text = productItem.product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp
                )

                Text(
                    text = productItem.product.weight,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 14.sp
                )

                Box(modifier = modifier.fillMaxSize()) {
                    if (productItem.count == 0) {
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
                                    text = "${productItem.product.priceCurrent} ₽",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                if (productItem.product.priceOld != null) {
                                    LineThroughText(
                                        text = productItem.product.priceOld,
                                        textStyle = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    } else {
                        Counter(
                            count = productItem.count.toString(),
                            onClickPlus = { onClickAddToBasket() },
                            onClickMinus = { onClickRemoveFromBasket() }
                        )
                    }
                }
            }
        }
    }
}