package com.hi88.app

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home_screen")
    data object RollDiceScreen : Screen("roll_the_dice_screen")
    data object GenerateNumberScreen : Screen("generate_number_screen")
    data object RandomListScreen : Screen("random_list_screen")
    data object SettingsScreen : Screen("settings_screen")
    data object HistoryScreen : Screen("history_screen")
    data object HelpScreen : Screen("help_screen")
    data object EditListScreen : Screen("edit_list/{listId}") {
        fun createRoute(listId: Int) = "edit_list/$listId"
    }

    data object ListEntriesScreen : Screen("list_entries/{listId}")
    data object AddEntryScreen : Screen("add_entry/{listId}") {
        fun createRoute(listId: Int) = "add_entry/$listId"
    }

    data object RandomEntryScreen : Screen("random_entry/{listId}/{entryId}") {
        fun createRoute(listId: Int, entryId: Int) = "random_entry/$listId/$entryId"
    }
}