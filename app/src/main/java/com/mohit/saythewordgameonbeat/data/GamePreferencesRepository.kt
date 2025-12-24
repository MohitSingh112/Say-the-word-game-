package com.mohit.saythewordgameonbeat.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mohit.saythewordgameonbeat.emuns.GameMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.get

val Context.dataStore by preferencesDataStore("game_preferences")


@Singleton
class GamePreferencesRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    private val KEY_GAME_MODE = intPreferencesKey("game_mode")

     fun getGameMode(): Flow<GameMode> {
        val savedMode =  context.dataStore.data.map { preference ->
            val modeIndex = preference[KEY_GAME_MODE] ?: 0
            if (modeIndex == 1) GameMode.TwoPlayer else GameMode.SinglePlayer
        }

        return savedMode
    }

    suspend fun saveGameMode(gameMode: GameMode) {
        context.dataStore.edit { preference ->
            preference[KEY_GAME_MODE] = if (gameMode == GameMode.SinglePlayer) 0 else 1
        }
    }
}

