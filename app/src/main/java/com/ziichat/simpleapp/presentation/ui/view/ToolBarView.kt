package com.ziichat.simpleapp.presentation.ui.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.ziichat.simpleapp.R
import com.ziichat.simpleapp.utils.NavigationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBarView(
    title: String,
    onAddClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = { NavigationUtils.popBackStack() }) {
                IconView(
                    R.drawable.ic_back_previous,
                    size = 24
                )
            }
        },
        actions = {
            if (onAddClick != null)
                TextButton(onClick = onAddClick) {
                    Text("Add")
                }
        }
    )
}