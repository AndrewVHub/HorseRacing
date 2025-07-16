package ru.andrewvhub.horseracing.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.andrewvhub.horseracing.db.dao.RacingHistoryDao
import ru.andrewvhub.horseracing.db.entity.RaceResultEntity
import ru.andrewvhub.horseracing.db.typeConverter.RaceStatusConverter

@Database(
    entities = [
        RaceResultEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RaceStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun racingHistoryDao(): RacingHistoryDao

    companion object {
        const val NAME = "app_database"
    }
}