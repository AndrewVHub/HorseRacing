package ru.andrewvhub.horseracing.domain.useCase

import kotlinx.coroutines.flow.firstOrNull
import ru.andrewvhub.horseracing.core.UseCase
import ru.andrewvhub.horseracing.data.model.RaceStatus
import ru.andrewvhub.horseracing.db.entity.toEntity
import ru.andrewvhub.horseracing.domain.repositories.RacingRepository
import timber.log.Timber

class CheckAndFixStuckRacesUseCase(
    private val repository: RacingRepository
): UseCase<Unit, Unit>() {

    override suspend fun execute(params: Unit) {
        val currentTime = System.currentTimeMillis()
        val thresholdTime = currentTime - 7_000L // 7 секунд назад

        // Получаем гонки со статусом RUNNING, которые "зависли"
        val stuckRaces = repository.getStuckRaces(RaceStatus.RUNNING, thresholdTime)
            .firstOrNull() // Берем первый список, который испустит Flow

        stuckRaces?.forEach { stuckRace ->
            val updatedRace = stuckRace.copy(
                status = RaceStatus.STUCK
            )
            repository.updateRaceResult(updatedRace.toEntity())
            Timber.tag("OS4:StuckRaceFix")
                .d("Обновлена зависшая гонка: ${stuckRace.timestamp} на статус STUCK")
        }
    }
}