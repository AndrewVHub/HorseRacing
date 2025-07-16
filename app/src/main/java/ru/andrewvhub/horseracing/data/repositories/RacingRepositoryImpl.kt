package ru.andrewvhub.horseracing.data.repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import ru.andrewvhub.horseracing.data.model.RaceResult
import ru.andrewvhub.horseracing.data.model.RaceStatus
import ru.andrewvhub.horseracing.data.model.toModel
import ru.andrewvhub.horseracing.db.dao.RacingHistoryDao
import ru.andrewvhub.horseracing.db.entity.RaceResultEntity
import ru.andrewvhub.horseracing.domain.repositories.RacingRepository

class RacingRepositoryImpl(
    private val racingDao: RacingHistoryDao
): RacingRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getRaceHistoryFlow(): Flow<List<RaceResult>> {
        return racingDao.getContactAsFlow().mapLatest {
            it.map { entity -> entity.toModel() }
        }
    }

    override suspend fun save(raceResult: RaceResultEntity) {
        racingDao.save(raceResult)
    }

    override suspend fun updateRaceResult(raceResult: RaceResultEntity) {
        racingDao.updateRaceResult(raceResult)
    }

    override fun getStuckRaces(status: RaceStatus, thresholdTime: Long): Flow<List<RaceResult>> {
        return racingDao.getRacesByStatusAndTimestampThreshold(status.name, thresholdTime)
            .map { entities -> entities.map { it.toModel() } }
    }
}