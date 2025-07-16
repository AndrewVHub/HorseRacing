package ru.andrewvhub.horseracing.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.andrewvhub.horseracing.data.model.RaceResult
import ru.andrewvhub.horseracing.data.model.RaceStatus

@Entity(tableName = RaceResultEntity.TABLE_NAME)
data class RaceResultEntity(
    @PrimaryKey val timestamp: Long,
    val winner: String,
    val firstHorseName: String,
    val secondHorseName: String,
    val firstHorseTimeMs: Long,
    val secondHorseTimeMs: Long,
    val status: RaceStatus,
    val distance: Double
) {
    companion object {
        const val TABLE_NAME = "History"
    }
}

fun RaceResult.toEntity() = RaceResultEntity(
    timestamp = timestamp,
    winner = winner,
    firstHorseName = firstHorseName,
    secondHorseName = secondHorseName,
    firstHorseTimeMs = firstHorseTimeMs,
    secondHorseTimeMs = secondHorseTimeMs,
    status = status,
    distance = distance
)