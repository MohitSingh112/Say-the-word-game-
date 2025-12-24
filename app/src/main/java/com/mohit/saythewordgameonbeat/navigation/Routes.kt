package com.mohit.saythewordgameonbeat.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {
    @Serializable
    data object MenuScreen: Route, NavKey




}