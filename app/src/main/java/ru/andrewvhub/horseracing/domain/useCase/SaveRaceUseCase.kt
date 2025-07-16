package ru.andrewvhub.horseracing.domain.useCase

import ru.andrewvhub.horseracing.core.UseCase
import ru.andrewvhub.horseracing.data.model.RaceResult
import ru.andrewvhub.horseracing.db.entity.toEntity
import ru.andrewvhub.horseracing.domain.repositories.RacingRepository

class SaveRaceUseCase(
    private val repository: RacingRepository
): UseCase<RaceResult, Unit>() {
    override suspend fun execute(params: RaceResult) {
        repository.save(params.toEntity())
    }
}