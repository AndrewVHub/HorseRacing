package ru.andrewvhub.horseracing.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCase<in Params, out Result>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    protected abstract suspend fun execute(params: Params): Result

    suspend operator fun invoke(params: Params): Result = withContext(coroutineDispatcher) {
        execute(params)
    }
}