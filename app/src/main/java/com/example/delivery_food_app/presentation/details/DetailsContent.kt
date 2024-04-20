package com.example.delivery_food_app.presentation.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delivery_food_app.R
import com.example.delivery_food_app.domain.entity.Product

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    component: DetailsComponent
) {
    val state by component.model.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) { paddingValues ->

        when (val currentState = state.productState) {
            is DetailsStore.State.ProductState.Error -> {}
            is DetailsStore.State.ProductState.Initial -> {}
            is DetailsStore.State.ProductState.Loaded -> {
                when (currentState.product) {
                    null -> {
//                        ProductNotFound()
                        TODO("реализовать функцию если product == nul")
                    }

                    else -> {
                        Content(
                            product = currentState.product,
                            paddingValues = paddingValues,
                            onClickAddToBasket = { component.onClickAddToBasket(currentState.product) },
                            onClickBack = {
                                component.onClickBack()
                            }
                        )
                    }
                }
            }

            is DetailsStore.State.ProductState.Loading -> {
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
}


@Composable
private fun Content(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    product: Product,
    onClickBack: () -> Unit,
    onClickAddToBasket: () -> Unit
) {
    Box(modifier = modifier.padding(paddingValues)) {
        Column(
            modifier = modifier.verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box {
                IconButton(
                    modifier = modifier
                        .padding(start = 16.dp, top = 16.dp)
                        .shadow(elevation = 1.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .background(color = Color.White)
                        .size(44.dp),
                    onClick = { onClickBack() },
                ) {
                    Icon(
                        modifier = modifier.size(24.dp),
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }

                Image(
                    modifier = modifier
                        .width(375.dp),
                    painter = painterResource(id = R.drawable.prod_item),
                    contentDescription = null
                )
            }

            Box(
                modifier = modifier
            ) {
                ProductInfoDetail(product = product)
            }
        }

        Box(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(72.dp)
                .background(color = MaterialTheme.colorScheme.background)
                .shadow(elevation = 1.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp)
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
                onClick = { onClickAddToBasket() }
            ) {
                Text(
                    text = stringResource(R.string.in_basket, product.priceCurrent),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ProductInfoDetail(
    modifier: Modifier = Modifier,
    product: Product
) {
    Column {
        Column(
            modifier = modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = modifier.height(24.dp))
        HorizontalDivider(thickness = 2.dp, color = DividerDefaults.color)

        InfoItem(name = "Вес", value = product.weight)
        InfoItem(name = "Энерг. ценность", value = product.energyValue)
        InfoItem(name = "Белки", value = product.proteins)
        InfoItem(name = "Жиры", value = product.fats)
        InfoItem(name = "Углеводы", value = product.carbohydrates)
        Spacer(modifier = modifier.height(80.dp))
    }
}

@Composable
private fun InfoItem(
    modifier: Modifier = Modifier,
    name: String,
    value: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
        ) {
            Text(
                modifier = modifier.weight(1f),
                text = name,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    HorizontalDivider(thickness = 2.dp, color = DividerDefaults.color)
}