package com.hi88.app.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hi88.app.Screen
import com.hi88.app.game.R
import com.hi88.app.home.components.TabRow
import com.hi88.app.util.RollitTopAppBar


private val tabs_info = listOf(
    R.string.roll_the_dice to R.drawable.dice_icon,
    R.string.generate_number to R.drawable.hash,
    R.string.random_list to R.drawable.list
)

private val destinations = listOf(
    Screen.RollDiceScreen.route,
    Screen.GenerateNumberScreen.route,
    Screen.RandomListScreen.route
)

@Composable
fun HomeScreen(
    navController: NavController,
) {
    Scaffold(
        topBar = {
            RollitTopAppBar(
                title = R.string.random,
                showNavigationIcons = true,
                icon = R.drawable.history,
                navController = navController
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .then(Modifier.padding(16.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs_info.forEachIndexed { index, (text, icon) ->
                TabRow(
                    text = text,
                    icon = icon,
                    navController = navController,
                    destination = destinations[index]
                )
            }
        }
    }
}