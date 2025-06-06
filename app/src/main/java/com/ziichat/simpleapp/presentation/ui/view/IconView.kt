package com.ziichat.simpleapp.presentation.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconView(
    iconRes: Int,
    size: Int,
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp,
    onclick: (() -> Unit)? = null,
) {
    var modifier = Modifier
        .size(size.dp)
        .padding(
            start = start,
            top = top,
            end = end,
            bottom = bottom
        )
        .clip(CircleShape)
        .let { if (onclick != null) it.clickable { onclick() } else it }

    Icon(
        painterResource(iconRes),
        null,
        modifier = modifier,
        tint = Color.Unspecified,
    )
}