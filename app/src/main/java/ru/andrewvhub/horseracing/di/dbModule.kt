package ru.andrewvhub.horseracing.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.andrewvhub.horseracing.db.AppDatabase

val dbModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            AppDatabase.NAME
        ).build()
    }

    single { get<AppDatabase>().racingHistoryDao() }
}