package com.hi88.app.util

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hi88.app.ui.theme.DarkBlue

@Composable
fun NavigationIcon(
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    tint: Color = DarkBlue
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                painter = painterResource(icon),
                modifier = Modifier.size(28.dp),
                tint = tint,
                contentDescription = null
            )
        }
    )
}