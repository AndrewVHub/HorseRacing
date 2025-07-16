package ru.andrewvhub.horseracing.di

import org.koin.dsl.module
import ru.andrewvhub.horseracing.domain.useCase.CheckAndFixStuckRacesUseCase
import ru.andrewvhub.horseracing.domain.useCase.GetRacingHistoryUseCaseAsFlow
import ru.andrewvhub.horseracing.domain.useCase.SaveRaceUseCase
import ru.andrewvhub.horseracing.domain.useCase.UpdateRaceUseCase

val useCaseModule = module {
    single { GetRacingHistoryUseCaseAsFlow(get()) }
    single { UpdateRaceUseCase(get()) }
    single { SaveRaceUseCase(get()) }
    single { CheckAndFixStuckRacesUseCase(get()) }
}