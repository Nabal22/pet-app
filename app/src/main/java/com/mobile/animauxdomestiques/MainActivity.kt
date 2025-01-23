package com.mobile.animauxdomestiques

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.filled.TypeSpecimen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mobile.animauxdomestiques.ui.theme.AnimauxDomestiquesTheme
import com.mobile.animauxdomestiques.datastore.SettingsDataStore
import com.mobile.animauxdomestiques.navigation.Screen
import com.mobile.animauxdomestiques.ui.components.dialog.DialogManager
import com.mobile.animauxdomestiques.ui.screens.SearchScreen
import com.mobile.animauxdomestiques.ui.screens.animal.AnimalListScreen
import com.mobile.animauxdomestiques.ui.screens.animal.AnimalScreen
import com.mobile.animauxdomestiques.ui.screens.SettingsScreen
import com.mobile.animauxdomestiques.ui.screens.specie.SpecieListScreen
import com.mobile.animauxdomestiques.ui.screens.activity.ActivityListScreen
import com.mobile.animauxdomestiques.ui.screens.activity.ActivityScreen
import com.mobile.animauxdomestiques.ui.screens.specie.SpecieScreen
import com.mobile.animauxdomestiques.ui.theme.components.customIconButtonColor
import com.mobile.animauxdomestiques.utils.createActivityChannel
import com.mobile.animauxdomestiques.utils.isPortrait
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var settingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createActivityChannel(this)
        enableEdgeToEdge()

        settingsDataStore = SettingsDataStore(applicationContext)
        val darkThemeFlow = settingsDataStore.darkMode
        val fontSizeFlow = settingsDataStore.fontSize

        setContent {
            val darkTheme by darkThemeFlow.collectAsState(initial = isSystemInDarkTheme())
            val fontSize by fontSizeFlow.collectAsState(initial = 8f)

            MainScreen(
                initialDarkTheme = darkTheme,
                initialFontSize = fontSize,
                onDarkModeToggle = { settingsDataStore.setDarkTheme(it) },
                onFontSizeChange = { settingsDataStore.setFontSize(it) }
            )
        }
    }
}

@Composable
fun MainScreen(
    model: MainViewModel = viewModel(),
    initialDarkTheme :Boolean,
    initialFontSize: Float,
    onDarkModeToggle: suspend (Boolean) -> Unit,
    onFontSizeChange: suspend (Float) -> Unit
) {

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ Log.d("permissions", if(it)"granted" else "denied")}

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val coroutineScope = rememberCoroutineScope()

    model.loadDefaultSpecies()

    AnimauxDomestiquesTheme (darkTheme = initialDarkTheme, fontSize = initialFontSize) {
        Scaffold(
            topBar = {
                if (currentRoute != null && currentRoute != Screen.SearchScreen.route) {
                    TopAppBarMain(model, navController, currentRoute)
                }
            },
            bottomBar = {
                BottomBar(navController)
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            floatingActionButton = {
                if (currentRoute == Screen.AnimalListScreen.route
                    || currentRoute == Screen.ActivityListScreen.route
                    || currentRoute == Screen.SpecieListScreen.route
                    ) {
                    FloatingActionButton(
                        onClick = {
                            when (currentRoute) {
                                Screen.AnimalListScreen.route ->{
                                    model.clearAnimalInput()
                                    model.showFullScreenDialog.value = true
                                }
                                Screen.ActivityListScreen.route ->{
                                    model.clearActivityInput()
                                    model.showFullScreenDialog.value = true
                                }
                                Screen.SpecieListScreen.route ->{
                                    model.showFullScreenDialog.value = true
                                }
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Element"
                        )
                    }
                }
            }
        ) { paddingValues ->
            DialogManager(model, navController)
            NavHost(
                navController = navController,
                startDestination = Screen.AnimalListScreen.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(
                    route = Screen.AnimalListScreen.route,
                ) {
                    AnimalListScreen(
                        model,
                        navController,
                        snackbarHostState
                    )
                }

                composable(
                    route = Screen.ActivityListScreen.route,
                ) {
                    ActivityListScreen(
                        model,
                        navController,
                        snackbarHostState
                    )
                }

                composable(
                    route = Screen.SpecieListScreen.route,
                ) {
                    SpecieListScreen(
                        model,
                        navController,
                        snackbarHostState
                    )
                }

                composable(
                    route = Screen.SearchScreen.route,
                ) {
                    SearchScreen(
                        model,
                        Modifier.fillMaxSize(),
                        navController
                    )
                }

                composable(
                    route = Screen.AnimalScreen.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
                ) {
                    AnimalScreen(model, Modifier.fillMaxSize(), navController, snackbarHostState)
                }

                composable(
                    route = Screen.ActivityScreen.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
                ) {
                    ActivityScreen(model, Modifier.fillMaxSize(), navController, snackbarHostState)
                }

                composable(
                    route = Screen.SpecieScreen.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
                ) {
                    SpecieScreen(model, Modifier.fillMaxSize(), navController, snackbarHostState)
                }

                composable(
                    route = Screen.SettingsScreen.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
                ) {
                    SettingsScreen(
                        isDarkModeEnabled = initialDarkTheme,
                        fontSize = initialFontSize,
                        onDarkModeToggle = { toggleValue ->
                            coroutineScope.launch {
                                onDarkModeToggle(toggleValue)
                            }
                        },
                        onFontSizeChange = { newSize ->
                            coroutineScope.launch {
                                onFontSizeChange(newSize)
                            }
                        }
                    )

                }

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarMain(model: MainViewModel, navController: NavHostController, currentRoute: String) {
    var title by remember { mutableStateOf("") }

    if (currentRoute == Screen.AnimalListScreen.route) title = "Vos animaux"
    if (currentRoute == Screen.ActivityListScreen.route) title = "Vos activités"
    if (currentRoute == Screen.SpecieListScreen.route) title = "Vos espèces"
    if (currentRoute == Screen.SettingsScreen.route)  title = "Réglages"

    TopAppBar(
        modifier = Modifier
            .then(if (!isPortrait()) Modifier.height(70.dp) else Modifier),
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = {
            if (currentRoute == Screen.AnimalListScreen.route
                || currentRoute == Screen.ActivityListScreen.route
                || currentRoute == Screen.SpecieListScreen.route
                ) {
                Text(
                    text= title,
                    fontSize = 35.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (currentRoute == Screen.SettingsScreen.route) {
                Row (
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(title)
                }
            }
        },
        navigationIcon = {
            if(currentRoute == Screen.AnimalScreen.route){
                model.setActivityConfigurationFormSelected(null)
            }
            if (currentRoute !== Screen.AnimalListScreen.route
                && currentRoute !== Screen.ActivityListScreen.route
                && currentRoute !== Screen.SpecieListScreen.route
                ) {
                IconButton(
                    colors = customIconButtonColor(),
                    onClick = { navController.popBackStack() })
                {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }
        },
        actions = {
            when (currentRoute) {
                Screen.AnimalListScreen.route, Screen.ActivityListScreen.route, Screen.SpecieListScreen.route -> {
                    IconButton(
                        colors = customIconButtonColor(),
                        onClick = { navController.navigate(Screen.SettingsScreen.route) }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Réglages"
                        )
                    }
                }
                Screen.AnimalScreen.route, Screen.ActivityScreen.route, Screen.SpecieScreen.route -> {
                    IconButton(
                        colors = customIconButtonColor(),
                        onClick = {
                            model.showFullScreenDialog.value = true
                        })
                    {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Modification"
                        )
                    }
                    IconButton(
                        colors = customIconButtonColor(),
                        onClick = {
                            model.showDeleteDialog.value = true
                        })
                    {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Suppression"
                        )
                    }
                }
                else -> {
                    IconButton(
                        onClick = {}
                    ) {
                        // Empty button to center title
                    }
                }
            }

        },
    )
}


@Composable
fun BottomBar(navController: NavHostController) = NavigationBar(
    containerColor = MaterialTheme.colorScheme.secondaryContainer,
    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    modifier = Modifier
        .then(if (!isPortrait()) Modifier.height(70.dp) else Modifier)
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBarItem(
        selected = currentRoute == Screen.AnimalListScreen.route,
        onClick = {
            if (currentRoute != Screen.AnimalListScreen.route) {
                navController.navigate(Screen.AnimalListScreen.route) {
                    launchSingleTop = true
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = false
                    }
                }
            }
        },
        icon = { Icon(Icons.Default.Pets, Screen.AnimalListScreen.route) }
    )
    NavigationBarItem(
        selected = currentRoute == Screen.ActivityListScreen.route,
        onClick = {
            if (currentRoute != Screen.ActivityListScreen.route) {
                navController.navigate(Screen.ActivityListScreen.route) {
                    launchSingleTop = true
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = false
                    }
                }
            }
        },
        icon = { Icon(Icons.Default.SportsBaseball, Screen.ActivityListScreen.route) }
    )
    NavigationBarItem(
        selected = currentRoute == Screen.SpecieListScreen.route,
        onClick = {
            if (currentRoute != Screen.SpecieListScreen.route) {
                navController.navigate(Screen.SpecieListScreen.route) {
                    launchSingleTop = true
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = false
                    }
                }
            }
        },
        icon = { Icon(Icons.Default.TypeSpecimen, "Specie") }
    )
    NavigationBarItem(
        selected = currentRoute == Screen.SearchScreen.route,
        onClick = {
            if (currentRoute != Screen.SearchScreen.route) {
                navController.navigate(Screen.SearchScreen.route) {
                    launchSingleTop = true
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = false
                    }
                }
            }
        },
        icon = { Icon(Icons.Default.Search, Screen.SearchScreen.route) }
    )
}
