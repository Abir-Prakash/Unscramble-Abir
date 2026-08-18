package com.example.unscramble.ui.theme


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var currentWord: String

    private var usedWords = mutableSetOf<String>()

    var userGuess by mutableStateOf("")
        private set

    init {
        resetGame()
    }

    fun updateUserGuess(guess: String) {
        userGuess = guess
    }

    fun checkUserGuess() {

        if (userGuess.equals(currentWord, ignoreCase = true)) {

            updateGameState(_uiState.value.score + SCORE_INCREASE)

        } else {

            _uiState.value = _uiState.value.copy(
                isGuessedWordWrong = true
            )
        }

        userGuess = ""
    }

    fun skipWord() {
        updateGameState(_uiState.value.score)
        userGuess = ""
    }

    fun resetGame() {

        usedWords.clear()

        _uiState.value = GameUiState(
            currentScrambledWord = pickRandomWordAndShuffle()
        )
    }

    private fun updateGameState(updatedScore: Int) {

        if (usedWords.size == MAX_NO_OF_WORDS) {

            _uiState.value = _uiState.value.copy(
                score = updatedScore,
                isGameOver = true,
                isGuessedWordWrong = false
            )

        } else {

            _uiState.value = _uiState.value.copy(
                currentScrambledWord = pickRandomWordAndShuffle(),
                currentWordCount = _uiState.value.currentWordCount + 1,
                score = updatedScore,
                isGuessedWordWrong = false
            )
        }
    }

    private fun pickRandomWordAndShuffle(): String {

        currentWord = allWords.random()

        while (usedWords.contains(currentWord)) {
            currentWord = allWords.random()
        }

        usedWords.add(currentWord)

        return shuffleWord(currentWord)
    }

    private fun shuffleWord(word: String): String {

        val tempWord = word.toCharArray()

        do {
            tempWord.shuffle()
        } while (String(tempWord) == word)

        return String(tempWord)
    }
}



