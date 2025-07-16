package ru.andrewvhub.horseracing.core

import kotlinx.coroutines.flow.Flow

abstract class UseCaseAsFlow<in Params, Result> {
    protected abstract fun execute(params: Params): Flow<Result>

    operator fun invoke(params: Params): Flow<Result> {
        return execute(params)
    }
}