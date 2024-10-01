package com.example.mainproject.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import com.example.mainproject.models.BottomNavItem

object Constant {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Filled.Home,
            route = "home"
        ),
        BottomNavItem(
            label = "Second",
            icon = Icons.Filled.Build,
            route = "second"
        ),
        BottomNavItem(
            label = "Third",
            icon = Icons.Filled.Build,
            route = "third"
        )
    )
}
