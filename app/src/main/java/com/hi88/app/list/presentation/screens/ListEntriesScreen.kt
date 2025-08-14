package com.hi88.app.list.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hi88.app.Screen
import com.hi88.app.game.R
import com.hi88.app.list.data.preferences.getSelectedEntries
import com.hi88.app.list.data.preferences.saveSelectedEntries
import com.hi88.app.list.presentation.ListViewModel
import com.hi88.app.list.presentation.components.AddButton
import com.hi88.app.list.presentation.components.DialogWindow
import com.hi88.app.ui.theme.DarkBlue
import com.hi88.app.util.RollitTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListEntriesScreen(
    navController: NavController,
    listId: Int,
    listViewModel: ListViewModel = koinViewModel(),
) {
    val entries by listViewModel.currentEntries.collectAsState()
    val context = LocalContext.current

    val selectedEntriesState = remember { mutableStateOf(setOf<Int>()) }
    val selectedEntries by selectedEntriesState

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        context.getSelectedEntries().collect { savedEntries ->
            selectedEntriesState.value = savedEntries
            listViewModel.loadEntriesForList(listId)
        }

    }

    Scaffold(
        topBar = {
            RollitTopAppBar(
                title = R.string.list,
                showNavigationIcons = true,
                showActions = true,
                navController = navController,
                titleAlignment = TextAlign.Center
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(entries) { entry ->
                    val isSelected = selectedEntries.contains(entry.entryId)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color.LightGray else Color.White)
                            .clickable {
                                val newSelectedEntries = if (isSelected) {
                                    selectedEntries - entry.entryId
                                } else {
                                    selectedEntries + entry.entryId
                                }
                                selectedEntriesState.value = newSelectedEntries
                                CoroutineScope(Dispatchers.IO).launch {
                                    context.saveSelectedEntries(newSelectedEntries)
                                }
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = entry.content,
                            color = if (isSelected) Color.White else Color.Black,
                            fontSize = 16.sp
                        )

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = DarkBlue
                            )
                        }
                    }
                }

                item {
                    AddButton(
                        navController = navController,
                        screenDestination = Screen.AddEntryScreen.createRoute(listId)
                    )
                }
            }

            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    painter = painterResource(R.drawable.three_dots),
                    contentDescription = "Options",
                    modifier = Modifier.size(32.dp),
                    tint = Color.Gray
                )
            }

            DialogWindow(
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                listId = listId,
                listViewModel = listViewModel,
                selectedEntries = selectedEntries,
                onActionPerformed = {
                    listViewModel.loadEntriesForList(listId)
                },
                navController = navController
            )
        }
    }
}