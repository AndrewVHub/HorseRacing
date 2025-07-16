package ru.andrewvhub.horseracing.domain.useCase

import kotlinx.coroutines.flow.Flow
import ru.andrewvhub.horseracing.core.UseCaseAsFlow
import ru.andrewvhub.horseracing.data.model.RaceResult
import ru.andrewvhub.horseracing.domain.repositories.RacingRepository

class GetRacingHistoryUseCaseAsFlow(
    private val repository: RacingRepository
) : UseCaseAsFlow<Unit, List<RaceResult>>() {
    override fun execute(params: Unit): Flow<List<RaceResult>> = repository.getRaceHistoryFlow()
}