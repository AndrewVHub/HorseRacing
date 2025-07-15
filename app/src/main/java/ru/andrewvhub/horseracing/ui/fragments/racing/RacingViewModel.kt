package ru.andrewvhub.horseracing.ui.fragments.racing

import android.util.Log
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
import kotlin.random.Random

class RacingViewModel: BaseViewModel() {

    // LiveData для отслеживания состояния гонки
    private val _raceState = MutableLiveData<RaceState>()
    val raceState: LiveData<RaceState> = _raceState

    // LiveData для событий UI, которые должны произойти во фрагменте
    private val _uiEvents = MutableLiveData<UIRacingEvent>()
    val uiEvents: LiveData<UIRacingEvent> = _uiEvents

    // Job для таймера гонки
    private var raceTimerJob: Job? = null

    // Список для хранения результатов гонок (для БД или истории)
    private val _raceHistory = MutableLiveData<List<RaceResult>>(listOf())
    val raceHistory: LiveData<List<RaceResult>> = _raceHistory

    val horseName1 = resources.getString(R.string.horse_name_1)
    val horseName2 = resources.getString(R.string.horse_name_2)

    init {
        _raceState.value = RaceState.Idle // Начальное состояние
    }

    fun startRace() {
        // защита от повторного старта
        if (_raceState.value != RaceState.Idle) {
            Log.d("OS4:RaceViewModel", "Гонка уже идет или в процессе сброса.")
            return
        }

        _raceState.value = RaceState.Running

        // 1) Генерим длительности для всех лошадей
        val horseNames = listOf(horseName1, horseName2)
        val durations: Map<String, Long> = horseNames.associateWith { Random.nextLong(5_000, 7_001) }
        _uiEvents.value = UIRacingEvent.StartAnimations(durations)

        // 2) Сортируем по времени, чтобы получить порядок финиша
        val sortedResults = durations
            .entries
            .sortedBy { it.value }      // сначала победитель, потом аутсайдер
        val finishedOrder = sortedResults.map { it.key }
        val winner = finishedOrder.first()

        // 3) Запускаем корутину для отправки событий «финиша» с нужными задержками
        raceTimerJob?.cancel()
        raceTimerJob = viewModelScope.launch {
            var elapsed = 0L
            for ((name, time) in sortedResults) {
                // ждём ровно столько, сколько осталось до финиша этой лошади
                delay(time - elapsed)
                elapsed = time
                _uiEvents.postValue(UIRacingEvent.HorseFinished(name))
            }

            // 4) После того, как обе лошади «финишировали», обновляем историю и состояние
            val result = RaceResult(
                timestamp = System.currentTimeMillis(),
                winner = winner,
                firstHorseTimeMs = sortedResults[0].value,
                secondHorseTimeMs = sortedResults[1].value,
                status = RaceStatus.FINISHED
            )

            val updatedHistory = listOf(result) + (_raceHistory.value.orEmpty())
            _raceHistory.postValue(updatedHistory)

            _raceState.postValue(RaceState.Finished)
            _uiEvents.postValue(UIRacingEvent.RaceEnded(winner))
        }
    }

    fun resetRace() {
        raceTimerJob?.cancel()
        _raceState.value = RaceState.Idle
        _uiEvents.value = UIRacingEvent.ResetUI
        Log.d("OS4:RaceViewModel", "Гонка сброшена.")
    }

    // Состояния гонки
    sealed class RaceState {
        data object Idle : RaceState()
        data object Running : RaceState()
        data object Finished : RaceState()
    }

    // События для UI, которые ViewModel отправляет во фрагмент
    sealed class UIRacingEvent {
        data class StartAnimations(val durations: Map<String, Long>) : UIRacingEvent()
        data class HorseFinished(val horseName: String) : UIRacingEvent()
        data class RaceEnded(val winnerName: String) : UIRacingEvent()
        data object ResetUI : UIRacingEvent()
    }

    // Для очистки ресурсов, когда ViewModel больше не нужна
    override fun onCleared() {
        super.onCleared()
        raceTimerJob?.cancel()
        Log.d("OS4:RaceViewModel", "ViewModel очищена.")
    }
}