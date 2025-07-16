package ru.andrewvhub.horseracing.di

import org.koin.dsl.module
import ru.andrewvhub.horseracing.data.repositories.RacingRepositoryImpl
import ru.andrewvhub.horseracing.domain.repositories.RacingRepository

val repositoryModule = module {
    single<RacingRepository> { RacingRepositoryImpl(get()) }
}