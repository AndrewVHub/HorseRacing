package ru.andrewvhub.horseracing.ui.fragments.racing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.andrewvhub.horseracing.R
import ru.andrewvhub.horseracing.core.BaseViewModel
import ru.andrewvhub.horseracing.data.model.RaceResult
import ru.andrewvhub.horseracing.data.model.RaceStatus
import ru.andrewvhub.horseracing.domain.useCase.CheckAndFixStuckRacesUseCase
import ru.andrewvhub.horseracing.domain.useCase.SaveRaceUseCase
import ru.andrewvhub.horseracing.domain.useCase.UpdateRaceUseCase
import kotlin.random.Random

class RacingViewModel(
    private val updateRaceUseCase: UpdateRaceUseCase,
    private val saveRaceUseCase: SaveRaceUseCase,
    private val checkAndFixStuckRacesUseCase: CheckAndFixStuckRacesUseCase
) : BaseViewModel() {

    // Состояние гонки
    private val _raceState = MutableLiveData<RaceState>(RaceState.Idle)
    val raceState: LiveData<RaceState> = _raceState

    // События UI
    private val _uiEvents = MutableLiveData<UIEvent>()
    val uiEvents: LiveData<UIEvent> = _uiEvents

    private var raceTimerJob: Job? = null
    private var raceStartTime: Long = 0L
    private var raceDistance: Double = 0.00

    //Вообще мысля есть убрать это поле, и при обновлении делать запрос к БД по primaryKey
    //Затем обновить только нужные поля
    private var currentRaceResult: RaceResult? = null

    val horseNames = listOf(
        resources.getString(R.string.horse_name_1),
        resources.getString(R.string.horse_name_2)
    )

    init {
        viewModelScope.launch {
            checkAndFixStuckRacesUseCase(Unit)
        }
    }

    /** Запуск гонки */
    fun startRace() {
        if (_raceState.value != RaceState.Idle) return

        raceStartTime = System.currentTimeMillis().also { _raceState.value = RaceState.Running }
        raceDistance = Random.nextInt(500, 1500) / 100.0
        val durations = horseNames.associateWith { Random.nextLong(5_000, 7_001) }
        _uiEvents.value = UIEvent.Start(durations)

        // Сохраняем факт старта с базовым результатом
        currentRaceResult = RaceResult(
            timestamp = raceStartTime,
            winner = "",
            firstHorseName = horseNames[0],
            secondHorseName = horseNames[1],
            firstHorseTimeMs = 0L,
            secondHorseTimeMs = 0L,
            status = RaceStatus.RUNNING,
            distance = raceDistance
        )
        saveResult(currentRaceResult)
        launchRace(durations)
    }

    /** Сброс гонки */
    fun resetRace() {
        raceTimerJob?.cancel()
        _raceState.value = RaceState.Idle
        _uiEvents.value = UIEvent.Reset
    }

    private fun launchRace(
        durations: Map<String, Long>,
        offset: Long = 0L
    ) {
        val sorted = durations.entries.sortedBy { it.value }
        val winner = sorted.first().key

        raceTimerJob = viewModelScope.launch {
            var elapsed = offset
            sorted.forEach { (name, fullTime) ->
                val wait = fullTime - elapsed
                if (wait > 0) {
                    delay(wait)
                    elapsed = fullTime
                }
                _uiEvents.postValue(UIEvent.HorseFinished(name))
            }

            val (first, second) = sorted

            val updatedRace = currentRaceResult?.copy(
                winner = winner,
                firstHorseTimeMs = first.value,
                secondHorseTimeMs = second.value,
                status = RaceStatus.FINISHED,
            )

            updatedRace?.let { updateRaceUseCase(it) }
            _raceState.postValue(RaceState.Finished)
            _uiEvents.postValue(UIEvent.Finish(winner))
        }
    }

    private fun saveResult(result: RaceResult?) {
        result?.let {
            viewModelScope.launch {
                saveRaceUseCase(result)
            }
        }
    }

    override fun onCleared() {
        raceTimerJob?.cancel()
        super.onCleared()
    }

    // Состояния гонки
    sealed class RaceState {
        data object Idle : RaceState()
        data object Running : RaceState()
        data object Finished : RaceState()
    }

    // События для UI
    sealed class UIEvent {
        data class Start(val durations: Map<String, Long>) : UIEvent()
        data class HorseFinished(val name: String) : UIEvent()
        data class Finish(val winner: String) : UIEvent()
        data object Reset : UIEvent()
    }
}
