package ru.andrewvhub.horseracing.ui.fragments.racing

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.andrewvhub.horseracing.R
import ru.andrewvhub.horseracing.core.BaseFragment
import ru.andrewvhub.horseracing.databinding.FragmentRacingBinding
import ru.andrewvhub.horseracing.ui.viewBinding.viewBinding
import ru.andrewvhub.utils.extensions.addSystemBottomSpace

class RacingFragment : BaseFragment(R.layout.fragment_racing) {

    private val viewBinding by viewBinding(FragmentRacingBinding::bind)
    override val viewModel by viewModel<RacingViewModel>()

    private var currentRaceAnimatorSet: AnimatorSet? = null

    private val rightEdgePaddingPx = 100f
    private var finishLineX: Float = 0f
    private var horseInitialX: Float = 0f


    private val horseViews: Map<String, LottieAnimationView> by lazy {
        mapOf(
            viewModel.horseName1 to viewBinding.horse1,
            viewModel.horseName2 to viewBinding.horse2
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(viewBinding) {
        super.onViewCreated(view, savedInstanceState)

        buttons.addSystemBottomSpace(false)

        horseInitialX = viewBinding.horse1.x
        val screenWidth = resources.displayMetrics.widthPixels.toFloat()
        val horseWidth = viewBinding.horse1.width.toFloat()
        finishLineX = screenWidth - rightEdgePaddingPx - horseWidth

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() = with(viewBinding) {
        startButton.setOnClickListener { viewModel.startRace() }
        restartButton.setOnClickListener { viewModel.resetRace() }
    }

    private fun setupObservers() {
        viewModel.uiEvents.observe(viewLifecycleOwner) { event ->
            when (event) {
                is RacingViewModel.UIRacingEvent.StartAnimations -> {
                    startRaceAnimations(event.durations)
                    viewBinding.startButton.isEnabled = false
                    viewBinding.restartButton.isEnabled = false
                    viewBinding.winnerTextView.text =
                        getString(R.string.racing_fragment_status_start_text)
                    Log.d("OS4:Race", "Гонка началась")
                }

                is RacingViewModel.UIRacingEvent.HorseFinished -> {
                    Log.d("OS4:Race", "Финиш: ${event.horseName}")
                }

                is RacingViewModel.UIRacingEvent.RaceEnded -> {
                    viewBinding.winnerTextView.text =
                        getString(R.string.racing_fragment_status_winner_text, event.winnerName)
                    viewBinding.restartButton.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.race_status_finished),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                RacingViewModel.UIRacingEvent.ResetUI -> {
                    resetUI()
                }
            }
        }

        viewModel.raceState.observe(viewLifecycleOwner) { event ->
            when (event) {
                RacingViewModel.RaceState.Idle -> {
                    Log.d("OS4:Race", "IDLE state")
                }

                RacingViewModel.RaceState.Running -> {
                    Log.d("OS4:Race", "RUNNING state")
                }

                RacingViewModel.RaceState.Finished -> {
                    Log.d("OS4:Race", "FINISHED state")
                }
            }
        }
    }

    private fun startRaceAnimations(durations: Map<String, Long>) {
        currentRaceAnimatorSet?.cancel()

        val animators = horseViews.map { (name, view) ->
            view.apply {
                progress = 0f
                translationX = 0f
                playAnimation()
            }
            createMoveAnimator(view, durations[name] ?: 7000L).also { animator ->
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.pauseAnimation()
                        Log.d("OS4:RaceFragment", "Лошадь $name финишировала на экране.")
                    }
                })
            }
        }

        currentRaceAnimatorSet = AnimatorSet().apply {
            playTogether(animators)
            start()
        }
    }

    private fun createMoveAnimator(
        target: View,
        durationMs: Long
    ): ObjectAnimator = ObjectAnimator.ofFloat(target, View.TRANSLATION_X, 0f, finishLineX).apply {
        duration = durationMs
        interpolator = LinearInterpolator()
    }

    private fun resetUI() {
        currentRaceAnimatorSet?.cancel()
        viewBinding.apply {
            backgroundImageView.translationX = 0f
            horseViews.values.forEach { view ->
                view.translationX = 0f
                view.progress = 0f
                view.pauseAnimation()
            }
            winnerTextView.text = getString(R.string.racing_fragment_status_waiting_start_text)
            startButton.isEnabled = true
            restartButton.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentRaceAnimatorSet?.cancel()
    }
}