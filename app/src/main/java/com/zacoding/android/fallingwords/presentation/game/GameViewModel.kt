package com.zacoding.android.fallingwords.presentation.game

import android.os.CountDownTimer
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zacoding.android.fallingwords.data.model.Question
import com.zacoding.android.fallingwords.data.model.UserActionFeedback
import com.zacoding.android.fallingwords.data.model.Word
import com.zacoding.android.fallingwords.data.onEmpty
import com.zacoding.android.fallingwords.data.onError
import com.zacoding.android.fallingwords.data.onLoading
import com.zacoding.android.fallingwords.data.onSuccess
import com.zacoding.android.fallingwords.domain.use_case.FetchAllWordsUseCase
import com.zacoding.android.fallingwords.presentation.base.*
import com.zacoding.android.fallingwords.util.FALLING_WORD_DURATION
import com.zacoding.android.fallingwords.util.USER_LIVES
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val fetchAllWordsUseCase: FetchAllWordsUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<BaseUIState> = MutableStateFlow(EmptyState)
    val uiState: StateFlow<BaseUIState> = _uiState

    @VisibleForTesting
    val allWordsList = ArrayList<Word>()

    private var score: Int = 0
    private var lives = USER_LIVES

    //current correct word
    var currentWordIndex = 0
    var correctAnswers = 0
    var wrongAnswers = 0
    var noAttempts = 0

    /**
     * QuestionModel: represent the (word & translation) that appear to user
     * and the translation of QuestionModel maybe correct or wrong
     */
    private lateinit var currentQuestion: Question

    private val _question: MutableStateFlow<Question?> = MutableStateFlow(null)
    val question = _question.asStateFlow()

    private val _userScore: MutableStateFlow<Int> = MutableStateFlow(score)
    val userScore = _userScore.asStateFlow()

    private val _userLives: MutableStateFlow<Int> = MutableStateFlow(lives)
    val userLives = _userLives.asStateFlow()

    private val _userActionFeedback: MutableStateFlow<UserActionFeedback> =
        MutableStateFlow(UserActionFeedback.DEFAULT)
    val userActionFeedback = _userActionFeedback.asStateFlow()

    private val _gameOverState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val gameOverState = _gameOverState.asStateFlow()

    private var timer: CountDownTimer? = null
    private val _timerState: MutableStateFlow<Long> =
        MutableStateFlow(FALLING_WORD_DURATION.toLong())
    val timerState = _timerState.asStateFlow()

    init {
        _userLives.value = lives
        loadWords()
    }

    private fun loadWords() {

        viewModelScope.launch {
            fetchAllWordsUseCase.invoke(Unit).collectLatest { dataResource ->

                dataResource.onSuccess {

                    allWordsList.addAll(data)
                    //shuffle words list so user can get new words each time (no in the same order)
                    allWordsList.shuffle()
                    sendNewWord()

                    _uiState.value = ContentState
                }.onLoading {
                    _uiState.value = LoadingState
                }.onEmpty {
                    _uiState.value = EmptyState
                }.onError {
                    _uiState.value = ErrorState(this.exception.message!!)
                }


            }
        }

    }

    /**
     * To show the CountDownTimer to select the answer in time.
     */
    fun startTimer() {
        timer = object : CountDownTimer((FALLING_WORD_DURATION.toLong()), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timerState.value = (millisUntilFinished / 1000)
            }

            override fun onFinish() {
                onNoAnswer()
            }
        }.start()

    }

    fun stopTimer() {
        timer?.cancel()
    }

    /**
     * If user doesn't select any answer
     */
    fun onNoAnswer() {
        _userLives.value = --lives
        _userActionFeedback.value = UserActionFeedback.NO_ATTEMPT
        noAttempts++
        //sendNewWord()
    }

    /**
     * If user select the current question translation is correct
     */
    fun onCorrectTransClicked() {
        var userActionFeedback = UserActionFeedback.CORRECT
        if (currentQuestion.translation == allWordsList[currentWordIndex].textSpanish) {
            score += 10
            _userScore.value = score
            correctAnswers++
        } else {
            _userLives.value = --lives
            wrongAnswers++
            userActionFeedback = UserActionFeedback.WRONG
        }

        _userActionFeedback.value = userActionFeedback
        //sendNewWord()
    }

    /**
     * If user select the current question translation is wrong
     */
    fun onWrongTransClicked() {
        var userActionFeedback = UserActionFeedback.CORRECT
        if (currentQuestion.translation != allWordsList[currentWordIndex].textSpanish) {
            score += 10
            _userScore.value = score
            correctAnswers++
        } else {
            _userLives.value = --lives
            wrongAnswers++
            userActionFeedback = UserActionFeedback.WRONG
        }

        _userActionFeedback.value = userActionFeedback

        //sendNewWord()
    }

    fun restartGame() {
        lives = USER_LIVES
        score = 0
        correctAnswers = 0
        wrongAnswers = 0
        noAttempts = 0
        _userLives.value = lives
        _userScore.value = score
        _gameOverState.value = false

        sendNewWord()
    }

    /**
     * this function for loading new question and pass it to the view
     * and validating is the game should continue or GameOver
     */
    fun sendNewWord() {

        //handle the resume case, and check for wordsList.isEmpty()
        if (allWordsList.isEmpty())
            return

        //if user finish all words or lose all his lives
        if (currentWordIndex >= allWordsList.size || lives <= 0) {
            _gameOverState.value = true
            return
        }

        _userActionFeedback.value = UserActionFeedback.DEFAULT

        currentWordIndex++

        if (currentWordIndex == allWordsList.size - 1) { //handle last question in the list scenario
            //use the correct translation
            Question(
                allWordsList[currentWordIndex].textEnglish,
                allWordsList[currentWordIndex].textSpanish
            )
        } else {
            //generate random number to decide to show the correct translation or wrong one
            val rnd = if (allWordsList.size > 1) (0 until allWordsList.size).random() else 0
            currentQuestion = if (rnd % 2 == 0) {
                //use the correct translation
                Question(
                    allWordsList[currentWordIndex].textEnglish,
                    allWordsList[currentWordIndex].textSpanish
                )
            } else {
                //use wrong translation
                Question(allWordsList[currentWordIndex].textEnglish, allWordsList[rnd].textSpanish)
            }
        }

        _question.value = currentQuestion
    }
}
