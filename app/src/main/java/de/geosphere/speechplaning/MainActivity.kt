package de.geosphere.speechplaning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FolderShared
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.geosphere.speechplaning.ui.theme.SpeechPlaningTheme
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent

@Suppress("UnusedPrivateProperty")
class MainActivity :
    ComponentActivity(),
    KoinComponent {
    @Suppress("LongMethod")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContent {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

            SpeechPlaningTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

                Scaffold(
                    topBar = {
                        topBarComponents(scrollBehavior)
                    },
                    content = { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = BottomNavigationItem.tabs.first().route,
                            modifier = Modifier
                                .padding(5.dp)
                                .padding(innerPadding)
                        ) {
                            composable<Screen.PlaningRoute> {
                                HorizontalDivider()
                            }
                            composable<Screen.SpeakerRoute> {
                                HorizontalDivider()
                            }
                            composable<Screen.SpeechesRoute> {
                                HorizontalDivider()
                            }
                        }
                    },

                    bottomBar = {
                        bottomBarComponents(currentDestination, selectedItemIndex, navController)
                    },
                )
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun topBarComponents(scrollBehavior: TopAppBarScrollBehavior) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text(
                    "Centered Top App Bar",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            actions = {
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Localized description"
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )
    }

    @Composable
    private fun bottomBarComponents(
        currentDestination: NavDestination?,
        selectedItemIndex: Int,
        navController: NavHostController
    ) {
        var selectedItemIndex1 = selectedItemIndex
        NavigationBar {
            BottomNavigationItem.tabs.forEachIndexed { index, navItems ->
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any {
                        it.route == navItems.route::class.qualifiedName
                    } == true,
                    onClick = {
                        selectedItemIndex1 = index
                        navController.navigate(navItems.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        navController.navigate(navItems.route)
                    },
                    label = { Text(navItems.label) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (navItems.badgeCount != null) {
                                    Badge {
                                        Text(text = navItems.badgeCount.toString())
                                    }
                                } else if (navItems.hasNews) {
                                    Badge()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (selectedItemIndex1 == index) {
                                    navItems.selectedIcon
                                } else {
                                    navItems.unselectedIcon
                                },
                                contentDescription = navItems.label
                            )
                        }
                    }
                )
            }
        }
    }
}

@Serializable
private sealed class Screen {
    @Serializable
    data object SpeakerRoute : Screen()

    @Serializable
    data object PlaningRoute : Screen()

    @Serializable
    data object SpeechesRoute : Screen()
}

private data class BottomNavigationItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val route: Screen,
    val badgeCount: Int? = null,
) {
    companion object {
        val tabs = listOf(
            BottomNavigationItem(
                label = "Plan",
                selectedIcon = Icons.Filled.CalendarMonth,
                unselectedIcon = Icons.Outlined.CalendarMonth,
                route = Screen.PlaningRoute,
                hasNews = true,
            ),
            BottomNavigationItem(
                label = "Speakers",
                selectedIcon = Icons.Filled.FolderShared,
                unselectedIcon = Icons.Outlined.FolderShared,
                route = Screen.SpeakerRoute,
                hasNews = false,
            ),
            BottomNavigationItem(
                label = "Speeches",
                selectedIcon = Icons.AutoMirrored.Filled.ListAlt,
                unselectedIcon = Icons.AutoMirrored.Outlined.ListAlt,
                hasNews = false,
                route = Screen.SpeechesRoute,
                badgeCount = 45
            ),
        )
    }
}
