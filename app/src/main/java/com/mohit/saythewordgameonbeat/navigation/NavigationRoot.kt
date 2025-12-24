package com.mohit.saythewordgameonbeat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.mohit.saythewordgameonbeat.emuns.Game
import com.mohit.saythewordgameonbeat.ui.screen.chooseDifficultyScreen.ChooseDifficultyScreen
import com.mohit.saythewordgameonbeat.ui.screen.menuScreen.MenuScreen

@Composable
fun NavigationRoot() {

    val backStack = rememberNavBackStack(Route.MenuScreen)

    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            entry<Route.MenuScreen> {
                MenuScreen(
                    onBeatClicked = {
                        backStack.add(Route.ChooseDifficultyScreen(game = Game.BeatSpeak))
                    },
                    onSettingsClicked = {
                        backStack.add(Route.MenuScreen)
                    },//Todo
                    onTapClicked = {
                        backStack.add(Route.ChooseDifficultyScreen(game = Game.TapToBeat))
                    }
                )
            }

            entry<Route.ChooseDifficultyScreen> {
                ChooseDifficultyScreen(
                    onBack = {
                        // FIX: Only pop if this screen is actually currently on top
                        if (backStack.lastOrNull() is Route.ChooseDifficultyScreen) {
                            backStack.removeLastOrNull()
                        }
                    },
                    game = Game.BeatSpeak //Todo
                )
            }


        }
    )

}