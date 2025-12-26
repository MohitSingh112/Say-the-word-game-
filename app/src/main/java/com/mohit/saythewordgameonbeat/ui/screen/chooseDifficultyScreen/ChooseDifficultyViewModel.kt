package com.mohit.saythewordgameonbeat.ui.screen.chooseDifficultyScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohit.saythewordgameonbeat.emuns.Difficulty
import com.mohit.saythewordgameonbeat.emuns.GameMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

import javax.inject.Inject

@HiltViewModel
class ChooseDifficultyViewModel @Inject constructor() : ViewModel() {

    private var _difficulty = MutableStateFlow(Difficulty.None)
    val difficulty = _difficulty.asStateFlow()

    fun onDifficultyChanged(difficulty: Difficulty){
        _difficulty.value = difficulty
    }

    fun resetState(){
        _difficulty.value = Difficulty.None
    }

}



