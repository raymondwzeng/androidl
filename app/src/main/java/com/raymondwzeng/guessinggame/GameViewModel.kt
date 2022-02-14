package com.raymondwzeng.guessinggame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    //Stuff for the game itself
    val words = listOf("Wordl", "Horse")
    val chosenWord = words.random().uppercase() //Take random word from words and set to uppercase
    var correctGuesses = ""

    //Live data
    private val _frequencyMap = HashSet<Char>() //Hashset will run to see if a word exists within the space.
    private val _liveLives = MutableLiveData<Int>(0)
    private val _incorrectGuesses = MutableLiveData<String>("")
    private val _secretWordDisplay = MutableLiveData<String>()
    private val _mismatchedGuesses = MutableLiveData<String>("")
    private val _isGameOver = MutableLiveData<Boolean>(false)
    private val _maxLives = 6

    val isGameOver : LiveData<Boolean> get() = _isGameOver
    val lives : LiveData<Int> get() = _liveLives //For XML
    val incorrectGuesses : LiveData<String> get() = _incorrectGuesses //For XML
    val mismatchedGuesses : LiveData<String> get() = _mismatchedGuesses //For XML
    val maxLives get() = _maxLives //For XML
    val secretWordDisplay : LiveData<String> get() = _secretWordDisplay //For XML

    init {
        _secretWordDisplay.value = deriveSecretWordDisplay()
    }

    fun makeGuess(guess : String) {
//        if(guess.length == 1) {
//            if(chosenWord.contains(guess)) {
//                correctGuesses += guess
//                _secretWordDisplay.value = deriveSecretWordDisplay()
//            } else {
//                _incorrectGuesses.value += guess
//            }
//            //"Wordle" style, take a life away every time.
//            _liveLives.value = _liveLives.value?.plus(1) //If not null, call minus with value 1
//            _isGameOver.value = isWon() || isLost()
//        }
        if(guess.length != 1) { //New input thing
            for(i in guess.indices) {
                if(chosenWord[i] == guess[i]) {
                    correctGuesses += guess[i]
                    _mismatchedGuesses.value = _mismatchedGuesses.value?.replaceFirst(guess[i].toString(), "", true)
                } else if(_frequencyMap.contains(guess[i])) {
                    _mismatchedGuesses.value += guess[i]
                } else {
                    _incorrectGuesses.value += guess[i]
                }
            }
            //"Wordle" style, take a life away every time.
            _secretWordDisplay.value = deriveSecretWordDisplay()
            _liveLives.value = _liveLives.value?.plus(1) //If not null, call minus with value 1
            _isGameOver.value = isWon() || isLost()
        }
    }

    fun isWon() = chosenWord.equals(_secretWordDisplay.value, true)
    fun isLost() = _liveLives.value ?: 6 >= 6

    fun deriveSecretWordDisplay() : String {
        var display = ""
        chosenWord.forEach {
            display += checkLetter(it.toString())
            _frequencyMap.add(it) //Add character to frequencymap.
        }
        return display
    }

    fun checkLetter(str: String) = when (correctGuesses.contains(str)) {
        true -> str
        false -> "_"
    }

    fun wonLostMessage() : String {
        var message = ""
        message = if(isWon()) "You won!"
        else "You lost!"
        message += " The word was $chosenWord"
        return message
    }

}