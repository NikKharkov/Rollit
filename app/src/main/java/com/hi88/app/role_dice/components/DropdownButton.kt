package com.hi88.app.role_dice.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hi88.app.ui.theme.DarkBlue

@Composable
fun DropDownButton(
    options: List<Int>,
    onOptionSelected: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableIntStateOf(1) }

    Box(
        modifier = Modifier
            .background(DarkBlue, RoundedCornerShape(8.dp))
            .clickable { expanded = true }
    ) {
        Text(
            text = "${selectedOption}x",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .wrapContentSize()
                .background(DarkBlue)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "${option}x",
                            fontSize = 16.sp
                        )
                    },
                    onClick = {
                        selectedOption = option
                        expanded = false
                        onOptionSelected(option)
                    },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
            }
        }
    }
}



