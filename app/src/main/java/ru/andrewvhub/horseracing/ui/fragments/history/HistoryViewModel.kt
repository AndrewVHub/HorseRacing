package ru.andrewvhub.horseracing.ui.fragments.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.andrewvhub.horseracing.R
import ru.andrewvhub.horseracing.core.BaseViewModel
import ru.andrewvhub.horseracing.domain.useCase.GetRacingHistoryUseCaseAsFlow
import ru.andrewvhub.horseracing.ui.items.RaceHistoryItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryViewModel(
    getRacingHistoryUseCaseAsFlow: GetRacingHistoryUseCaseAsFlow
) : BaseViewModel() {
    private val dateFormatter = SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault())

    val raceHistoryItems: LiveData<List<RaceHistoryItem>> =
        getRacingHistoryUseCaseAsFlow(Unit)
            .map { raceResults ->
                raceResults.map { result ->
                    RaceHistoryItem(
                        timestamp = resources.getString(
                            R.string.item_history_start_time,
                            dateFormatter.format(Date(result.timestamp))
                        ),
                        winner = result.winner,
                        horseNames = resources.getString(
                            R.string.item_history_horses,
                            result.firstHorseName,
                            result.secondHorseName
                        ),
                        distance = resources.getString(
                            R.string.item_history_distance,
                            result.distance
                        ),
                        status = result.status
                    )
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                emptyList()
            )
            .asLiveData()
}