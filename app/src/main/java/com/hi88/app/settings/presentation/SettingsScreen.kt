package com.hi88.app.settings.presentation

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
import com.hi88.app.settings.data.SettingsRepository
import com.hi88.app.settings.presentation.components.SettingsRow
import com.hi88.app.util.RollitTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsRepository: SettingsRepository = koinInject()
) {
    val isDarkTheme by settingsRepository.isDarkThemeFlow.collectAsState(initial = false)
    val areAnimationsEnabled by settingsRepository.areAnimationsEnabledFlow.collectAsState(initial = true)
    val isSoundEnabled by settingsRepository.isSoundEnabledFlow.collectAsState(initial = true)

    Scaffold(
        topBar = {
            RollitTopAppBar(
                title = R.string.settings,
                titleAlignment = TextAlign.Center,
                navController = navController,
                showActions = true
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
            SettingsRow(
                checked = isDarkTheme,
                text = "Dark theme",
                onCheckedChange = { enabled ->
                    CoroutineScope(Dispatchers.IO).launch {
                        settingsRepository.setDarkTheme(enabled)
                    }
                }
            )

            SettingsRow(
                checked = areAnimationsEnabled,
                text = "Animations",
                onCheckedChange = { enabled ->
                    CoroutineScope(Dispatchers.IO).launch {
                        settingsRepository.setAnimationsEnabled(enabled)
                    }
                }
            )
            SettingsRow(
                checked = isSoundEnabled,
                text = "Sound and vibration",
                onCheckedChange = { enabled ->
                    CoroutineScope(Dispatchers.IO).launch {
                        settingsRepository.setSoundEnabled(enabled)
                    }
                }
            )
            SettingsRow(
                text = "Help / FAQ",
                onCheckedChange = {},
                icon = R.drawable.forward_icon,
                onClick = {
                    navController.navigate("help_screen")
                }
            )
        }
    }
}
