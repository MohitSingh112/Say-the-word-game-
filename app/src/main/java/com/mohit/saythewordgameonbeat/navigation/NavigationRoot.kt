package com.mohit.saythewordgameonbeat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.mohit.saythewordgameonbeat.ui.screen.menuScreen.MenuScreen

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {

    val backStack = rememberNavBackStack(Route.MenuScreen)

    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.removeLast()
        },
        entryProvider = entryProvider {
            entry<Route.MenuScreen>{
                MenuScreen(
                    onBeatClicked = {
                        backStack.add(Route.MenuScreen)
                    },//Todo
                    onSettingsClicked = {
                        backStack.add(Route.MenuScreen)
                    },//Todo
                    onTapClicked = {
                        backStack.add(Route.MenuScreen)
                    }//Todo
                )
            }
        }
    )
    
}