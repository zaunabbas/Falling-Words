package com.zacoding.android.fallingwords.presentation.game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.annotation.VisibleForTesting
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zacoding.android.fallingwords.R
import com.zacoding.android.fallingwords.data.model.Question
import com.zacoding.android.fallingwords.data.model.UserActionFeedback
import com.zacoding.android.fallingwords.databinding.FragmentFallingWordsGameBinding
import com.zacoding.android.fallingwords.presentation.base.ContentState
import com.zacoding.android.fallingwords.presentation.base.EmptyState
import com.zacoding.android.fallingwords.presentation.base.ErrorState
import com.zacoding.android.fallingwords.presentation.base.LoadingState
import com.zacoding.android.fallingwords.util.FALLING_WORD_DURATION
import com.zacoding.android.fallingwords.util.showErrorMessageInDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class FallingWordsGameFragment : Fragment() {

    private var _binding: FragmentFallingWordsGameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @VisibleForTesting
    internal val gameViewModel: GameViewModel by viewModels()

    private var objectFallingAnimator: ObjectAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFallingWordsGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        observeState()
        observeData()
    }

    private fun initUI() {
        setupUiEventListeners()
    }

    private fun setupUiEventListeners() {

        binding.btnWrong.setOnClickListener {
            gameViewModel.onWrongTransClicked()
        }

        binding.btnCorrect.setOnClickListener {
            gameViewModel.onCorrectTransClicked()
        }
        binding.layoutGameOver.btnRestartGame.setOnClickListener {
            onGameRestart()
        }
    }

    private fun observeState() {
        lifecycleScope.launchWhenStarted {
            gameViewModel.uiState.collect { uiState ->
                when (uiState) {
                    is LoadingState -> {
                        //do nothing
                    }

                    is ContentState -> {
                        //do nothing
                    }

                    is EmptyState -> {
                        //do nothing
                    }

                    is ErrorState -> {
                        activity?.showErrorMessageInDialog(errorMessage = uiState.message)
                    }

                }
            }
        }
    }

    private fun observeData() {

        lifecycleScope.launchWhenStarted {
            gameViewModel.question.collectLatest {
                it?.let {
                    startFallingAnswer(it)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            gameViewModel.userScore.collectLatest {
                binding.tvUserScore.text = it.toString()
            }
        }

        lifecycleScope.launchWhenStarted {
            gameViewModel.userLives.collectLatest {
                binding.tvUserLives.text = it.toString()
            }
        }

        lifecycleScope.launchWhenStarted {
            gameViewModel.timerState.collectLatest {
                binding.tvCounter.text = it.toString()
            }
        }

        lifecycleScope.launchWhenStarted {
            gameViewModel.userActionFeedback.collectLatest {
                if (it != UserActionFeedback.DEFAULT) {
                    stopFallingAnimation()
                    when (it) {
                        UserActionFeedback.CORRECT -> {
                            binding.ivAnswer.setImageResource(R.drawable.ic_baseline_check_24)
                        }
                        UserActionFeedback.WRONG,
                        UserActionFeedback.NO_ATTEMPT -> {
                            binding.ivAnswer.setImageResource(R.drawable.ic_baseline_close_24)
                        }
                        else -> {
                            //do nothing
                        }
                    }

                    binding.ivAnswer.isVisible = true
                    binding.ivAnswer.postDelayed(
                        {
                            binding.ivAnswer.isVisible = false
                            gameViewModel.sendNewWord()
                        }, 1000
                    )
                }

            }
        }

        lifecycleScope.launchWhenStarted {
            gameViewModel.gameOverState.collectLatest {
                if (it) {
                    onGameOver()
                }
            }
        }

    }

    private fun onGameRestart() {
        binding.layoutGameOver.rootView.isVisible = false
        binding.clHeaderInfoContainer.isVisible = true
        binding.clBottomActionContainer.isVisible = true
        gameViewModel.restartGame()
    }

    private fun onGameOver() {
        stopFallingAnimation()
        binding.clBottomActionContainer.isVisible = false
        binding.clHeaderInfoContainer.isVisible = false
        binding.layoutGameOver.rootView.isVisible = true
        binding.layoutGameOver.tvUserScore.text = gameViewModel.userScore.value.toString()
        binding.layoutGameOver.tvCorrectAnswersCount.text = gameViewModel.correctAnswers.toString()
        binding.layoutGameOver.tvWrongAnswersCount.text = gameViewModel.wrongAnswers.toString()
        binding.layoutGameOver.tvNoAttemptsCount.text = gameViewModel.noAttempts.toString()

    }

    override fun onResume() {
        super.onResume()
        gameViewModel.sendNewWord()
    }

    override fun onPause() {
        super.onPause()
        stopFallingAnimation()
    }

    private fun startFallingAnimation() {
        objectFallingAnimator?.start()
        gameViewModel.startTimer()
        toggleActionButtons(true)
    }

    private fun stopFallingAnimation() {
        objectFallingAnimator?.cancel()
        toggleActionButtons(false)
        gameViewModel.stopTimer()
    }

    private fun startFallingAnswer(question: Question) {
        binding.tvFallingWord.isVisible = true
        binding.tvWord.text = question.question
        binding.tvFallingWord.text = question.translation
        val yValueStart = binding.tvFallingWord.height
        val yValueEnd: Int =
            this.view?.bottom?.minus((binding.tvFallingWord.height + binding.clBottomActionContainer.height + binding.clHeaderInfoContainer.height))
                ?: 0
        stopFallingAnimation()
        objectFallingAnimator = ObjectAnimator.ofFloat(
            binding.tvFallingWord,
            View.TRANSLATION_Y,
            yValueStart.toFloat(),
            yValueEnd.toFloat()
        ).setDuration(FALLING_WORD_DURATION.toLong())
        objectFallingAnimator?.interpolator = LinearInterpolator()
        objectFallingAnimator?.addListener(objectFallingAnimationListener)
        startFallingAnimation()
    }

    private val objectFallingAnimationListener = object : AnimatorListenerAdapter() {

        override fun onAnimationStart(animation: Animator) {
            binding.tvFallingWord.isVisible = true
        }

        override fun onAnimationEnd(animation: Animator) {
            binding.tvFallingWord.isVisible = false
        }
    }

    private fun toggleActionButtons(isClickable: Boolean) {
        binding.btnCorrect.isEnabled = isClickable
        binding.btnWrong.isEnabled = isClickable
    }
}
