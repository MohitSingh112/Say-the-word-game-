package com.mohit.saythewordgameonbeat.navigation

import androidx.navigation3.runtime.NavKey
import com.mohit.saythewordgameonbeat.emuns.Difficulty
import com.mohit.saythewordgameonbeat.emuns.Game
import com.mohit.saythewordgameonbeat.emuns.GameMode
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {
    @Serializable
    data object MenuScreen: Route, NavKey

    @Serializable
    data class ChooseDifficultyScreen(
        val gameName: Game,
        val gameMode: GameMode
    ): Route, NavKey

    @Serializable
    data object SettingScreen: Route, NavKey

    @Serializable
    data class  TapToBeatGameScreen(
        val difficulty: Difficulty,
        val gameMode: GameMode
    ): Route, NavKey

}