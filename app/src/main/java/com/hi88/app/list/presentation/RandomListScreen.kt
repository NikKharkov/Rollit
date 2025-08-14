package com.hi88.app.list.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hi88.app.Screen
import com.hi88.app.game.R
import com.hi88.app.list.presentation.components.AddButton
import com.hi88.app.list.presentation.components.ListItemRow
import com.hi88.app.util.RollitTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun RandomListScreen(
    navController: NavController,
    listViewModel: ListViewModel = koinViewModel(),
) {
    val lists by listViewModel.lists.collectAsState()

    Scaffold(
        topBar = {
            RollitTopAppBar(
                title = R.string.list,
                icon = R.drawable.flag_icon,
                showNavigationIcons = true,
                showActions = true,
                titleAlignment = TextAlign.Center,
                navController = navController
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .then(Modifier.padding(16.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(lists) { list ->
                ListItemRow(
                    navController = navController,
                    destination = "list_entries/${list.id}",
                    listItem = list
                )
            }
            item {
                AddButton(
                    navController = navController,
                    screenDestination = Screen.EditListScreen.createRoute(-1)
                )
            }
        }
    }
}
