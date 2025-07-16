package ru.andrewvhub.horseracing.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.andrewvhub.horseracing.data.model.RaceResult
import ru.andrewvhub.horseracing.data.model.RaceStatus
import ru.andrewvhub.horseracing.db.entity.RaceResultEntity

interface RacingRepository {
    fun getRaceHistoryFlow(): Flow<List<RaceResult>>
    suspend fun save(raceResult: RaceResultEntity)
    suspend fun updateRaceResult(raceResult: RaceResultEntity)
    fun getStuckRaces(status: RaceStatus, thresholdTime: Long): Flow<List<RaceResult>>
}