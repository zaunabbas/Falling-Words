package com.zacoding.android.fallingwords.presentation.game

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zacoding.android.fallingwords.MainCoroutinesRule
import com.zacoding.android.fallingwords.data.DataResource
import com.zacoding.android.fallingwords.data.model.Word
import com.zacoding.android.fallingwords.domain.use_case.FetchAllWordsUseCase
import com.zacoding.android.fallingwords.util.USER_LIVES
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class GameViewModelTest {

    // Subject under test
    private lateinit var viewModel: GameViewModel
    private val mockAllWordsList = getMockWordsData()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var fetchAllWordsUseCase: FetchAllWordsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // When
        coEvery { fetchAllWordsUseCase.invoke(Unit) }
            .returns(flowOf(DataResource.Success(mockAllWordsList)))
        // Invoke
        viewModel = GameViewModel(fetchAllWordsUseCase)
    }


    @Test
    fun `test viewModel states initialized with default values`() = runTest {
        val lives = viewModel.userLives.value
        val score = viewModel.userScore.value

        assertEquals(lives, USER_LIVES)
        assertEquals(score, 0)
    }

    @Test
    fun `test loading word list after create viewModel instance`() = runTest {
        assertEquals(viewModel.allWordsList.size, mockAllWordsList.size)
    }

    @Test
    fun `test select the correct answer will increase the score`() = runTest {
        val currentWord = viewModel.allWordsList[viewModel.currentWordIndex]
        val question = viewModel.question.value

        if(question?.translation == currentWord.textSpanish)
            viewModel.onCorrectTransClicked()
        else
            viewModel.onWrongTransClicked()

        val score = viewModel.userScore.value
        assertEquals(score,10)
    }

    private fun getMockWordsData(): List<Word> {
        return listOf(
            Word("holidays", "vacaciones"),
            Word("group", "grupo"),
            Word("ball", "balón"),
            Word("jungle", "jungla")
        )
    }

}


