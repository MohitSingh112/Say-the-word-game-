package com.mohit.saythewordgameonbeat.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import com.mohit.saythewordgameonbeat.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeatPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // --- 1. Background Music (MediaPlayer) ---
    private var musicPlayer: MediaPlayer? = null

    // --- 2. Short SFX (SoundPool) ---
    private val soundPool: SoundPool
    private val tikSoundId: Int
    private val tokSoundId: Int

    init {
        // Initialize SoundPool for fast playback
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5) // Allow 5 sounds at once
            .setAudioAttributes(audioAttributes)
            .build()

        // PRE-LOAD SOUNDS (Important for zero latency)
        // Make sure these files exist in res/raw
        tikSoundId = soundPool.load(context, R.raw.tik, 1)
        tokSoundId = soundPool.load(context, R.raw.tok, 1)
    }

    // --- SFX FUNCTIONS ---
    fun playTik() {
        soundPool.play(tikSoundId, 1f, 1f, 1, 0, 1f)
    }

    fun playTok() {
        soundPool.play(tokSoundId, 1f, 1f, 1, 0, 1f)
    }


    // --- MUSIC FUNCTIONS (Same as before) ---
    fun playBackgroundMusic() {
        if (musicPlayer == null) {
            musicPlayer = MediaPlayer.create(context, R.raw.game_beat).apply {
                isLooping = true
                setVolume(0.4f, 0.4f) // Lower volume so SFX is louder
                start()
            }
        } else if (musicPlayer?.isPlaying == false) {
            musicPlayer?.start()
        }
    }

    fun pauseBackgroundMusic() {
        if (musicPlayer?.isPlaying == true) {
            musicPlayer?.pause()
        }
    }

    fun stopAll() {
        // Stop Music
        musicPlayer?.stop()
        musicPlayer?.release()
        musicPlayer = null
    }

    // Cleanup when app closes
    fun release() {
        stopAll()
        soundPool.release()
    }
}