package ru.andrewvhub.horseracing.data.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import ru.andrewvhub.horseracing.R

data class RaceResult(
    val timestamp: Long,
    val winner: String,
    val firstHorseTimeMs: Long,
    val secondHorseTimeMs: Long,
    val status: RaceStatus
)

enum class RaceStatus(
    @StringRes val nameResId: Int,
    @ColorRes val colorStatus: Int
) {
    FINISHED(R.string.race_status_finished, R.color.green_pastel),
    RUNNING(R.string.race_status_running, R.color.yellow_pastel),
    UNKNOWN(R.string.race_status_unknown, R.color.red_pastel)
}