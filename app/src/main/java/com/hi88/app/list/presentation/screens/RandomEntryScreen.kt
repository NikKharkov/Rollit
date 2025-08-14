package com.hi88.app.list.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hi88.app.game.R
import com.hi88.app.list.presentation.ListViewModel
import com.hi88.app.util.RollitTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun RandomEntryScreen(
    navController: NavController,
    listId: Int,
    entryId: Int,
    listViewModel: ListViewModel = koinViewModel()
) {
    val entries by listViewModel.currentEntries.collectAsState()

    LaunchedEffect(listId) {
        listViewModel.loadEntriesForList(listId)
    }

    val selectedEntry = entries.find { it.entryId == entryId }

    Scaffold(
        topBar = {
            RollitTopAppBar(
                title = R.string.random_entry,
                showNavigationIcons = true,
                showActions = true,
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedEntry != null) {
                Text(
                    text = selectedEntry.content,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "Entry not found",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
