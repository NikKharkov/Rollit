package com.hi88.app.history.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hi88.app.game.R
import com.hi88.app.generate_number.GenerateNumberViewModel
import com.hi88.app.history.data.repository.HistoryRepository
import com.hi88.app.history.data.repository.SavedRangesRepository
import com.hi88.app.history.presentation.components.HistoryRow
import com.hi88.app.history.presentation.components.SavedListsRow
import com.hi88.app.history.presentation.components.SavedRangesRow
import com.hi88.app.list.presentation.ListViewModel
import com.hi88.app.util.RollitTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun HistoryScreen(
    navController: NavController,
    generateNumberViewModel: GenerateNumberViewModel = koinViewModel(),
    listViewModel: ListViewModel = koinViewModel(),
    savedRangesRepository: SavedRangesRepository = koinInject(),
    historyRepository: HistoryRepository = koinInject(),
) {
    val historyItems by historyRepository.getHistoryFlow().collectAsState(initial = emptyList())
    val savedRanges by savedRangesRepository.getSavedRangesFlow().collectAsState(initial = emptyList())
    val savedLists by listViewModel.lists.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            RollitTopAppBar(
                title = R.string.history,
                showNavigationIcons = false,
                showActions = true,
                titleAlignment = TextAlign.Center,
                navController = navController
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .then(Modifier.padding(16.dp)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HistoryRow(
                text = "Roll the dice",
                items = historyItems,
                onDelete = { timestamp ->
                    CoroutineScope(Dispatchers.IO).launch {
                        historyRepository.removeHistoryItem(timestamp)
                    }
                }
            )

            SavedRangesRow(
                text = "Saved ranges",
                items = savedRanges,
                onDelete = { timestamp ->
                    CoroutineScope(Dispatchers.IO).launch {
                        savedRangesRepository.removeSavedRange(timestamp)
                    }
                },
                onSelect = { range ->
                    generateNumberViewModel.applyPreset(range)
                    navController.navigate("generate_number_screen")
                }
            )

            SavedListsRow(
                text = "Saved lists",
                items = savedLists,
                onDelete = { list ->
                    listViewModel.deleteList(list)
                },
                onSelect = { list ->
                    navController.navigate("list_entries/${list.id}")
                }
            )
        }
    }
}