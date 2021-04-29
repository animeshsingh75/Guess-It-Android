package com.example.android.guesstheword.screens.game

import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel


private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel : ViewModel() {
    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }
    companion object{
        private const val DONE=0L
        private const val ONE_SECOND=1000L
        private const val COUNTDOWN_PANIC_SECONDS = 10L
        private const val COUNTDOWN_TIME=60000L
    }
    // The current word
    private val timer:CountDownTimer
    private val _word = MutableLiveData<String>()
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime
    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score
    val word: LiveData<String>
        get() = _word
    private val _eventbuzz = MutableLiveData<BuzzType>()
    val eventbuzz: LiveData<BuzzType>
        get() = _eventbuzz
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    val currentTimeString=Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        _eventGameFinish.value = false
        _score.value = 0
        _currentTime.value=0L
        _word.value = ""
        resetList()
        nextWord()
        timer=object :CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value=millisUntilFinished/ ONE_SECOND
                if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                    _eventbuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _currentTime.value= DONE
                _eventbuzz.value = BuzzType.GAME_OVER
                _eventGameFinish.value=true
            }

        }
        timer.start()
    }
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    fun onSkip() {
        _score.value = score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = score.value?.plus(1)
        _eventbuzz.value=BuzzType.CORRECT
        nextWord()
    }

    private fun nextWord() {
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    override fun onCleared() {
        Log.i("GameViewModel", "GameViewModel Destroyed")
        timer.cancel()
        super.onCleared()
    }
    fun onBuzzComplete(){
        _eventbuzz.value=BuzzType.NO_BUZZ
    }
    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }
}