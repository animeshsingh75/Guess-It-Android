package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController

class ScoreViewModel(finalScore:Int):ViewModel() {
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score
    private val _eventGameRestart = MutableLiveData<Boolean>()
    val eventGameRestart: LiveData<Boolean>
        get() = _eventGameRestart
    init {
        _score.value=finalScore
        _eventGameRestart.value=false
    }
     fun onPlayAgain() {
        _eventGameRestart.value=true

    }
   fun onPlayAgainComplete(){
        _eventGameRestart.value=false
    }
}