package com.hi88.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hi88.app.settings.data.SettingsRepository
import com.hi88.app.ui.theme.RollItTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsRepository: SettingsRepository = koinInject()
            val isDarkTheme by settingsRepository.isDarkThemeFlow.collectAsState(initial = false)
            RollItTheme(darkTheme = isDarkTheme) {
                RollitApp()
            }
        }
    }
}