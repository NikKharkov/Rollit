package com.hi88.app.list.presentation.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hi88.app.Screen
import com.hi88.app.game.R
import com.hi88.app.list.data.preferences.saveSelectedEntries
import com.hi88.app.list.presentation.ListViewModel
import com.hi88.app.util.CustomRowWithCheckbox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun DialogWindow(
    navController: NavController,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    listId: Int,
    listViewModel: ListViewModel = koinViewModel(),
    selectedEntries: Set<Int>,
    onActionPerformed: (Set<Int>) -> Unit,
) {
    val context = LocalContext.current

    if (showDialog) {
        var randomSelectChecked by remember { mutableStateOf(false) }
        var excludeSelectedChecked by remember { mutableStateOf(false) }
        var shuffleChecked by remember { mutableStateOf(false) }
        var newSelectedEntries by remember { mutableStateOf(selectedEntries) }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = null,
            text = {
                Column(
                    modifier = Modifier
                        .background(Color(0xFFD9D9D9))
                        .wrapContentSize()
                ) {
                    CustomRowWithCheckbox(
                        text = R.string.randomly_select,
                        checked = randomSelectChecked,
                        onCheckedChange = { isChecked ->
                            randomSelectChecked = isChecked
                            if (isChecked) {
                                val randomEntry = listViewModel.selectRandomEntry(
                                    excludeSelectedChecked,
                                    newSelectedEntries
                                )
                                if (randomEntry != null) {
                                    if (excludeSelectedChecked) {
                                        newSelectedEntries =
                                            newSelectedEntries + randomEntry.entryId
                                        CoroutineScope(Dispatchers.IO).launch {
                                            context.saveSelectedEntries(newSelectedEntries)
                                        }
                                    }
                                    onActionPerformed(newSelectedEntries)
                                    navController.navigate(Screen.RandomEntryScreen.createRoute(listId, randomEntry.entryId))
                                } else {
                                    Toast.makeText(
                                        context,
                                        "No available entries",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    )

                    CustomRowWithCheckbox(
                        text = R.string.exclude_already_selected,
                        checked = excludeSelectedChecked,
                        onCheckedChange = {
                            listViewModel.deleteSelectedEntries(listId, selectedEntries)
                            onDismiss()
                            onActionPerformed(selectedEntries)
                        }
                    )

                    CustomRowWithCheckbox(
                        text = R.string.shuffle,
                        checked = shuffleChecked,
                        onCheckedChange = { isChecked ->
                            shuffleChecked = isChecked
                            if (isChecked) {
                                listViewModel.shuffleEntries(listId)
                            }
                        }
                    )
                }
            },
            confirmButton = {},
            containerColor = Color(0xFFD9D9D9),
            shape = RoundedCornerShape(4.dp),
            textContentColor = Color.White
        )
    }
}

