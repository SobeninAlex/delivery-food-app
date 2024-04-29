package com.example.delivery_food_app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ComponentRegistry
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.size.Size
import com.arkivanov.decompose.defaultComponentContext
import com.example.delivery_food_app.DeliveryFoodApplication
import com.example.delivery_food_app.R
import com.example.delivery_food_app.presentation.root.DefaultRootComponent
import com.example.delivery_food_app.presentation.root.RootComponent
import com.example.delivery_food_app.presentation.root.RootContent
import com.example.delivery_food_app.presentation.ui.theme.SplashBackgroundColor
import kotlinx.coroutines.delay
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    private val appComponent by lazy {
        (applicationContext as DeliveryFoodApplication).applicationComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)

        installSplashScreen()

        val root = rootComponentFactory.create(defaultComponentContext())

        setContent {
            Navigation(root = root)
        }
    }
}

@Composable
private fun SplashScreen(
    navController: NavController
) {

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components(fun ComponentRegistry.Builder.() {
            add(GifDecoder.Factory())
        })
        .build()

    LaunchedEffect(key1 = true) {
        delay(1800)
        navController.popBackStack()
        navController.navigate("main_screen")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberImagePainter(
                imageLoader = imageLoader,
                data = R.drawable.logo_anim,
                builder = {
                    size(Size.ORIGINAL)
                }
            ),
            contentDescription = null,
        )
    }
}

@Composable
private fun Navigation(root: RootComponent) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") {
            SplashScreen(navController)
        }

        composable("main_screen") {
            RootContent(component = root)
        }
    }
}
