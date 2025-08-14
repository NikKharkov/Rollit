package com.hi88.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hi88.app.generate_number.GenerateNumberScreen
import com.hi88.app.history.presentation.HistoryScreen
import com.hi88.app.home.HomeScreen
import com.hi88.app.list.presentation.RandomListScreen
import com.hi88.app.list.presentation.screens.AddEntryScreen
import com.hi88.app.list.presentation.screens.EditListScreen
import com.hi88.app.list.presentation.screens.ListEntriesScreen
import com.hi88.app.list.presentation.screens.RandomEntryScreen
import com.hi88.app.role_dice.RollDiceScreen
import com.hi88.app.settings.presentation.SettingsScreen
import com.hi88.app.settings.presentation.screens.HelpScreen
import com.hi88.app.Screen

@Composable
fun RollitApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(Screen.RollDiceScreen.route) {
            RollDiceScreen(navController)
        }
        composable(Screen.GenerateNumberScreen.route) {
            GenerateNumberScreen(navController)
        }
        composable(Screen.RandomListScreen.route) {
            RandomListScreen(navController)
        }
        composable(Screen.SettingsScreen.route) {
            SettingsScreen(navController)
        }
        composable(Screen.HistoryScreen.route) {
            HistoryScreen(navController)
        }
        composable(Screen.HelpScreen.route) {
            HelpScreen(navController)
        }
        composable(
            Screen.EditListScreen.route,
            arguments = listOf(navArgument("listId") { type = NavType.IntType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getInt("listId") ?: -1
            EditListScreen(navController, listId)
        }
        composable(
            Screen.ListEntriesScreen.route,
            arguments = listOf(navArgument("listId") { type = NavType.IntType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getInt("listId") ?: -1
            ListEntriesScreen(navController, listId)
        }
        composable(
            Screen.AddEntryScreen.route,
            arguments = listOf(navArgument("listId") { type = NavType.IntType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getInt("listId") ?: -1
            AddEntryScreen(navController, listId)
        }
        composable(
            Screen.RandomEntryScreen.route,
            arguments = listOf(
                navArgument("listId") { type = NavType.IntType },
                navArgument("entryId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getInt("listId") ?: -1
            val entryId = backStackEntry.arguments?.getInt("entryId") ?: -1
            RandomEntryScreen(navController, listId, entryId)
        }
    }
}