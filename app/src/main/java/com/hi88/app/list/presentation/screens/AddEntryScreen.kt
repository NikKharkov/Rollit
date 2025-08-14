package com.hi88.app.list.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.xr.compose.testing.toDp
import com.hi88.app.game.R
import com.hi88.app.list.presentation.ListViewModel
import com.hi88.app.settings.data.SettingsRepository
import com.hi88.app.util.CustomButton
import com.hi88.app.util.RollitTopAppBar
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun AddEntryScreen(
    navController: NavController,
    listId: Int,
    listViewModel: ListViewModel = koinViewModel(),
    settingsRepository: SettingsRepository = koinInject(),
) {
    val context = LocalContext.current
    var content by remember { mutableStateOf("") }
    val isDarkTheme by settingsRepository.isDarkThemeFlow.collectAsState(initial = false)
    val textMeasurer = rememberTextMeasurer()
    val maxWidth = LocalConfiguration.current.screenWidthDp.dp - 32.dp

    Scaffold(
        topBar = {
            RollitTopAppBar(
                title = R.string.add_entry,
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
                .padding(16.dp)
        ) {
            BasicTextField(
                value = content,
                onValueChange = { newText ->
                    val textWidth = textMeasurer.measure(
                        newText,
                        TextStyle(fontSize = 20.sp)
                    ).size.width.toDp()

                    if (!newText.contains("\n") && textWidth < maxWidth) {
                        content = newText
                    }
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    color = if (isDarkTheme) (Color.White) else (Color.Black)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                decorationBox = { innerTextField ->
                    if (content.isEmpty()) {
                        Text("Enter new entry...", color = Color.Gray, fontSize = 16.sp)
                    }
                    innerTextField()
                },
                cursorBrush = if (isDarkTheme) SolidColor(Color.White) else SolidColor(Color.Black)
            )

            CustomButton(
                onClick = {
                    if (content.isNotBlank()) {
                        listViewModel.addEntry(listId, content)
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Entry can't be empty", Toast.LENGTH_SHORT).show()
                    }
                },
                text = R.string.save
            )
        }
    }
}
