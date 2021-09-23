package lt.kepo.airq

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import lt.kepo.airquality.airqualities.AirQualitiesListScreen
import lt.kepo.airquality.airqualitydetails.AirQualityDetailsScreen
import lt.kepo.stations.StationsScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 104)
        }

//        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    AirQTheme {
        val systemUiController = rememberSystemUiController()
        val systemBarsColor = MaterialTheme.colors.background
        val useDarkIcons = MaterialTheme.colors.isLight

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = systemBarsColor,
                darkIcons = useDarkIcons
            )
        }

        val navController = rememberNavController()

        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            NavHost(
                navController = navController,
                startDestination = "airQualitiesList",
            ) {
                composable(
                    route = "airQualitiesList",
                ) {
                    AirQualitiesListScreen(
                        navController = navController,
                        viewModel = hiltViewModel()
                    )
                }
                composable(
                    route = "airQualityDetails/{stationId}",
                    arguments = listOf(
                        navArgument("stationId") {
                            type = NavType.IntType
                        }
                    ),
                ) {
                    AirQualityDetailsScreen(
                        navController = navController,
                        viewModel = hiltViewModel()
                    )
                }
                composable(
                    route = "stations",
                ) {
                    StationsScreen(
                        navController = navController,
                        viewModel = hiltViewModel(),
                    )
                }
            }
        }
    }
}
