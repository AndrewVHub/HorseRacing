package ru.andrewvhub.horseracing.ui.fragments.racing

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.andrewvhub.horseracing.R
import ru.andrewvhub.horseracing.core.BaseFragment
import ru.andrewvhub.horseracing.databinding.FragmentRacingBinding
import ru.andrewvhub.horseracing.ui.viewBinding.viewBinding
import timber.log.Timber

class RacingFragment : BaseFragment(R.layout.fragment_racing) {

    private val viewBinding by viewBinding(FragmentRacingBinding::bind)
    override val viewModel by viewModel<RacingViewModel>()

    private var currentRaceAnimatorSet: AnimatorSet? = null

    private val finishLineX: Float by lazy {
        resources.displayMetrics.widthPixels.toFloat()
    }

    private val horseViews: Map<String, LottieAnimationView> by lazy {
        mapOf(
            viewModel.horseNames[0] to viewBinding.horse1,
            viewModel.horseNames[1] to viewBinding.horse2
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() = with(viewBinding) {
        startButton.setOnClickListener { viewModel.startRace() }
        restartButton.setOnClickListener { viewModel.resetRace() }
    }

    private fun setupObservers(): Unit = with(viewBinding) {
        viewModel.uiEvents.observe(viewLifecycleOwner) { event ->
            when (event) {
                is RacingViewModel.UIEvent.Start -> {
                    startRaceAnimations(event.durations)
                    startButton.isEnabled = false
                    restartButton.isEnabled = false
                    winnerTextView.text =
                        getString(R.string.racing_fragment_status_start_text)
                    Timber.tag("OS4:RacingFragment").d("Гонка началась")
                }

                is RacingViewModel.UIEvent.HorseFinished -> {
                    Timber.tag("OS4:RacingFragment").d("Финиш: ${event.name}")
                }

                is RacingViewModel.UIEvent.Finish -> {
                    winnerTextView.text =
                        getString(R.string.racing_fragment_status_winner_text, event.winner)
                    restartButton.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.race_status_finished),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                RacingViewModel.UIEvent.Reset -> { resetUI() }
            }
        }

        viewModel.raceState.observe(viewLifecycleOwner) { event ->
            when (event) {
                RacingViewModel.RaceState.Idle -> {
                    Timber.tag("OS4:RacingFragment").d("IDLE state")
                }

                RacingViewModel.RaceState.Running -> {
                    Timber.tag("OS4:RacingFragment").d("RUNNING state")
                }

                RacingViewModel.RaceState.Finished -> {
                    Timber.tag("OS4:RacingFragment").d("FINISHED state")
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
                        Timber.tag("OS4:RacingFragment").d("Лошадь $name финишировала на экране.")
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