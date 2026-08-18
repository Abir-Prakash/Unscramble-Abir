package com.example.unscramble.ui.test

import com.example.unscramble.ui.theme.GameViewModel
import com.example.unscramble.ui.theme.MAX_NO_OF_WORDS
import com.example.unscramble.ui.theme.SCORE_INCREASE
import com.example.unscramble.ui.theme.allWords
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Test
fun getUnscrambledWord(scrambledWord: String): String {
    return allWords.first { word ->
        word.toList().sorted() == scrambledWord.toList().sorted()
    }
}
class GameViewModelTest {
    private val veiwModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = veiwModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        veiwModel.updateUserGuess(correctPlayerWord)
        veiwModel.checkUserGuess()
        currentGameUiState = veiwModel.uiState.value
        assertEquals(20, currentGameUiState.score)
        assertFalse(currentGameUiState.isGuessedWordWrong)
    }

    @Test
    fun IncorrectGuess() {
        val viewModel = GameViewModel()
        val incorrectPlayerWord = "and"
        veiwModel.updateUserGuess(incorrectPlayerWord)
        veiwModel.checkUserGuess()
        val currentGameUiState = veiwModel.uiState.value
        assertEquals(0, currentGameUiState.score)
        assertTrue(currentGameUiState.isGuessedWordWrong)
    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val gameUiState = veiwModel.uiState.value
        val unScrambledWord = getUnscrambledWord(gameUiState.currentScrambledWord)
        assertNotEquals(unScrambledWord, gameUiState.currentScrambledWord)
        assertTrue(gameUiState.currentWordCount == 1)
        assertTrue(gameUiState.score == 0)
        assertFalse(gameUiState.isGuessedWordWrong)
    }
    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var expectedScore = 0
        var currentGameUiState = veiwModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            veiwModel.updateUserGuess(correctPlayerWord)
            veiwModel.checkUserGuess()
            currentGameUiState = veiwModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
            assertEquals(expectedScore, currentGameUiState.score)
        }
        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.currentWordCount)
        assertTrue(currentGameUiState.isGameOver)

    }
}


