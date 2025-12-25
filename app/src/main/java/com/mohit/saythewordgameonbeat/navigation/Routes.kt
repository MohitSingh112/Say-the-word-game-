package com.mohit.saythewordgameonbeat.navigation

import androidx.navigation3.runtime.NavKey
import com.mohit.saythewordgameonbeat.emuns.Game
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {
    @Serializable
    data object MenuScreen: Route, NavKey

    @Serializable
    data class ChooseDifficultyScreen(
        val game: Game
    ): Route, NavKey

    @Serializable
    data object SettingScreen: Route, NavKey
}