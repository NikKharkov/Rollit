package com.hi88.app.list.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hi88.app.game.R
import com.hi88.app.list.data.entity.ListItem
import com.hi88.app.list.presentation.ListViewModel
import com.hi88.app.settings.data.SettingsRepository
import com.hi88.app.util.CustomButton
import com.hi88.app.util.RollitTopAppBar
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun EditListScreen(
    navController: NavController,
    listId: Int,
    listViewModel: ListViewModel = koinViewModel(),
    settingsRepository: SettingsRepository = koinInject(),
) {
    val isDarkTheme by settingsRepository.isDarkThemeFlow.collectAsState(initial = false)
    val context = LocalContext.current
    val list by listViewModel.lists
        .map { lists -> lists.find { item -> item.id == listId } }
        .collectAsState(initial = null)

    var title by remember { mutableStateOf("") }

    LaunchedEffect(list) {
        title = list?.title ?: ""
    }

    Scaffold(
        topBar = {
            RollitTopAppBar(
                title = if (listId == -1) R.string.new_list else R.string.edit_list,
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
                value = title,
                onValueChange = { title = it },
                textStyle = TextStyle(
                    color = if (isDarkTheme) (Color.White) else (Color.Black),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) {
                        Text(
                            "Header",
                            color = Color.Gray,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    innerTextField()
                },
                cursorBrush = if (isDarkTheme) SolidColor(Color.White) else SolidColor(Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                onClick = {
                    if (title.isNotBlank()) {
                        if (listId == -1) {
                            listViewModel.addList(title)
                        } else {
                            listViewModel.updateList(ListItem(id = listId, title = title))
                        }
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Name can't be empty", Toast.LENGTH_SHORT).show()
                    }
                },
                text = R.string.save
            )
        }
    }
}