package com.mohit.saythewordgameonbeat.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.mohit.saythewordgameonbeat.emuns.Difficulty
import com.mohit.saythewordgameonbeat.emuns.Game
import com.mohit.saythewordgameonbeat.emuns.GameMode
import com.mohit.saythewordgameonbeat.ui.screen.chooseDifficultyScreen.ChooseDifficultyScreen
import com.mohit.saythewordgameonbeat.ui.screen.menuScreen.MenuScreen
import com.mohit.saythewordgameonbeat.ui.screen.settingScreen.SettingScreen
import com.mohit.saythewordgameonbeat.ui.screen.tapToBeatScreen.TapToBeatGameScreen

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
                    onBeatClicked = { gameMode ->
                        if(backStack.lastOrNull() !is Route.ChooseDifficultyScreen) {
                            backStack.add(Route.ChooseDifficultyScreen(
                                gameName = Game.BeatSpeak,
                                gameMode = gameMode
                            ))
                        }
                    },
                    onSettingsClicked = {
                        backStack.add(Route.SettingScreen)
                    },
                    onTapClicked = { gameMode ->
                        if(backStack.lastOrNull() !is Route.ChooseDifficultyScreen) {
                            backStack.add(Route.ChooseDifficultyScreen(
                                gameName = Game.TapToBeat,
                                gameMode = gameMode
                            ))
                        }
                    }
                )
            }

            entry<Route.ChooseDifficultyScreen> { game ->
                ChooseDifficultyScreen(
                    gameMode = game.gameMode,
                    onBack = {
                        // FIX: Only pop if this screen is actually currently on top
                        if (backStack.lastOrNull() is Route.ChooseDifficultyScreen) {
                            backStack.removeLastOrNull()
                        }
                    },
                    onStartClicked = { difficulty ->

                        if(backStack.lastOrNull() !is Route.TapToBeatGameScreen) {
                            backStack.add(Route.TapToBeatGameScreen(
                                gameMode = game.gameMode,
                                difficulty = difficulty
                            ))
                        }
                    }  //Todo
                )
            }

            entry<Route.SettingScreen> {
                val context = LocalContext.current

                SettingScreen(
                    onBack = {
                        if (backStack.lastOrNull() is Route.SettingScreen) {
                            backStack.removeLastOrNull()
                        }
                    },
                    onShareClicked = {
                        // 2. Share Logic (Directly here)
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_TEXT,
                                "Check out this game! https://play.google.com/store/apps/details?id=${context.packageName}"//Todo(paste right link)
                            )
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share via")
                        context.startActivity(shareIntent)
                    },
                    onFeedbackClicked = {
                        // 3. Feedback Logic (Directly here)
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:")
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("your.email@gmail.com")) //Todo(paste right email)
                            putExtra(Intent.EXTRA_SUBJECT, "Feedback: Say The Word Game")
                        }
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Handle case where no email app is found
                        }
                    }
                )
            }

            entry<Route.TapToBeatGameScreen>{
                TapToBeatGameScreen(
                    gameMode = it.gameMode,
                    difficulty = it.difficulty,
                    onBack = {
                        if(backStack.lastOrNull() is Route.TapToBeatGameScreen) backStack.removeLastOrNull()
                    }
                )
            }


        }
    )

}