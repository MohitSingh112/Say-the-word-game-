package com.mohit.saythewordgameonbeat

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.mohit.saythewordgameonbeat.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeatPlayer @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val soundPool: SoundPool

    // Sound IDs (The loaded file reference)
    private val gameSoundId: Int
    private val memorizationSoundId: Int
    private val tikSoundId: Int
    private val tokSoundId: Int

    // Stream IDs (The currently playing instance)
    private var musicStreamId: Int = 0

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        // Load all sounds
        gameSoundId = soundPool.load(context, R.raw.game_beat, 1)
        memorizationSoundId = soundPool.load(context, R.raw.memorization_beat, 1)
        tikSoundId = soundPool.load(context, R.raw.tik, 1)
        tokSoundId = soundPool.load(context, R.raw.tok, 1)
    }

    // --- MUSIC (LOOPING) ---

    fun playGameBackgroundMusic() {
        stopMusic() // Stop any previous music first

        // Loop parameter: -1 means loop forever
        musicStreamId = soundPool.play(gameSoundId, 0.5f, 0.5f, 1, 0, 0.9f)
    }

    fun playMemorizationMusic() {
        stopMusic()

        // Loop parameter: -1 means loop forever
        musicStreamId = soundPool.play(memorizationSoundId, 0.5f, 0.5f, 1, 0, 0.9f)
    }

    // --- SFX (ONE SHOT) ---

    fun playTik() {
        soundPool.play(tikSoundId, 1f, 1f, 1, 0, 1f)
    }

    fun playTok() {
        soundPool.play(tokSoundId, 1f, 1f, 1, 0, 1f)
    }

    // --- CONTROLS ---

    fun pauseBackgroundMusic() {
        // Use the STREAM ID to pause
        if (musicStreamId != 0) {
            soundPool.pause(musicStreamId)
        }
    }

    fun resumeBackgroundMusic() {
        // Use the STREAM ID to resume
        if (musicStreamId != 0) {
            soundPool.resume(musicStreamId)
        }
    }

    fun stopAll() {
        stopMusic()
        // Also stop any specific SFX if needed, though they usually finish naturally
    }

    private fun stopMusic() {
        if (musicStreamId != 0) {
            soundPool.stop(musicStreamId)
            musicStreamId = 0
        }
    }

    fun release() {
        stopAll()
        soundPool.release()
    }
}