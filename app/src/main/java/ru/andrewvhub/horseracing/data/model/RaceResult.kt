package ru.andrewvhub.horseracing.data.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import ru.andrewvhub.horseracing.R
import ru.andrewvhub.horseracing.db.entity.RaceResultEntity

data class RaceResult(
    val timestamp: Long,
    val winner: String,
    val firstHorseName: String,
    val secondHorseName: String,
    val firstHorseTimeMs: Long,
    val secondHorseTimeMs: Long,
    val status: RaceStatus,
    val distance: Double
)

enum class RaceStatus(
    @StringRes val nameResId: Int,
    @ColorRes val colorStatus: Int
) {
    FINISHED(R.string.race_status_finished, R.color.green_pastel),
    RUNNING(R.string.race_status_running, R.color.yellow_pastel),
    STUCK(R.string.race_status_unknown, R.color.red_pastel)
}

fun RaceResultEntity.toModel() = RaceResult(
    timestamp = timestamp,
    winner = winner,
    firstHorseName = firstHorseName,
    secondHorseName = secondHorseName,
    firstHorseTimeMs = firstHorseTimeMs,
    secondHorseTimeMs = secondHorseTimeMs,
    status = status,
    distance = distance
)