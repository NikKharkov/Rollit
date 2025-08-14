package com.hi88.app.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hi88.app.Screen
import com.hi88.app.game.R
import com.hi88.app.settings.data.SettingsRepository
import com.hi88.app.ui.theme.DarkBlue
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RollitTopAppBar(
    @StringRes title: Int,
    @DrawableRes icon: Int = R.drawable.history,
    showNavigationIcons: Boolean = false,
    showActions: Boolean = false,
    titleAlignment: TextAlign = TextAlign.Start,
    navController: NavController,
    settingsRepository: SettingsRepository = koinInject()
) {

    val isDarkTheme by settingsRepository.isDarkThemeFlow.collectAsState(initial = false)
    val textColor = if (isDarkTheme) Color.White else DarkBlue
    val iconColor = if (isDarkTheme) Color.White else DarkBlue

    TopAppBar(
        title = {
            Text(
                text = stringResource(title),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = titleAlignment,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            if (showActions) {
                NavigationIcon(
                    onClick = { navController.popBackStack() },
                    icon = R.drawable.arrow_back,
                    tint = iconColor
                )
            }
        },
        actions = {
            if (showNavigationIcons) {
                Row {
                    NavigationIcon(
                        onClick = {
                            navController.navigate(Screen.HistoryScreen.route)
                        },
                        icon = icon,
                        tint = iconColor
                    )
                    NavigationIcon(
                        onClick = {
                            navController.navigate(Screen.SettingsScreen.route)
                        },
                        icon = R.drawable.settings,
                        tint = iconColor
                    )
                }
            }
        },
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}



