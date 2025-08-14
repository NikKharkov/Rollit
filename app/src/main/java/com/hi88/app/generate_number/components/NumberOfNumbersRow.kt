package com.hi88.app.generate_number.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hi88.app.game.R

@Composable
fun NumberOfNumbersRow(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.number_of_numbers),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.padding(horizontal = 16.dp))

        CustomTextField(
            value = value,
            onValueChange = onValueChange,
        )
    }
    if (isError) {
        Row {
            Text(
                text = stringResource(R.string.invalid_number_count),
                color = Color.Red,
                fontSize = 12.sp
            )
        }
    }
}
