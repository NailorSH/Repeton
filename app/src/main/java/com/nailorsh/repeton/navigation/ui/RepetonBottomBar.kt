package com.nailorsh.repeton.navigation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nailorsh.repeton.core.ui.theme.BottomNavigationBarColor
import com.nailorsh.repeton.core.ui.theme.SelectedBottomBarIconColor
import com.nailorsh.repeton.core.ui.theme.UnselectedBottomBarIconColor
import com.nailorsh.repeton.navigation.AppSections


@Composable
fun RepetonBottomBar(
    tabs: Array<AppSections>,
    navController: NavController,
) {
    BottomAppBar(
        modifier = Modifier.height(55.dp),
        tonalElevation = 1.dp,
        contentPadding = PaddingValues(0.dp)
    ) {
        NavigationBar(
            containerColor = BottomNavigationBarColor,
        ) {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            tabs.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = {
                              navController.navigate(item.route)
                    },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(item.icon),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SelectedBottomBarIconColor,
                        unselectedIconColor = UnselectedBottomBarIconColor,
                        indicatorColor = BottomNavigationBarColor
                    )
                )
            }
        }
    }
}