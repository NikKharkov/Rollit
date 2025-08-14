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
fun ChooseRangeRow(
    valueFrom: String,
    valueTo: String,
    onValueChangeFrom: (String) -> Unit,
    onValueChangeTo: (String) -> Unit,
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
            text = stringResource(R.string.range_from),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.padding(horizontal = 8.dp))

        CustomTextField(
            value = valueFrom,
            onValueChange = onValueChangeFrom,
        )

        Text(
            text = stringResource(R.string.to),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        CustomTextField(
            value = valueTo,
            onValueChange = onValueChangeTo
        )
    }
    if (isError) {
        Row {
            Text(
                text = stringResource(R.string.invalid_range),
                color = Color.Red,
                fontSize = 12.sp
            )
        }
    }
}