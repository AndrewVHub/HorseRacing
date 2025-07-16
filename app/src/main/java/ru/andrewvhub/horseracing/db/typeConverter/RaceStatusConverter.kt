package ru.andrewvhub.horseracing.db.typeConverter

import androidx.room.TypeConverter
import ru.andrewvhub.horseracing.data.model.RaceStatus

class RaceStatusConverter {

    // Метод для преобразования RaceStatus в String для хранения в БД
    @TypeConverter
    fun fromRaceStatus(status: RaceStatus?): String? {
        return status?.name // Используем .name, чтобы получить строковое имя enum (например, "RUNNING")
    }

    // Метод для преобразования String из БД обратно в RaceStatus
    @TypeConverter
    fun toRaceStatus(statusName: String?): RaceStatus? {
        return statusName?.let { name ->
            try {
                RaceStatus.valueOf(name) // Преобразуем строковое имя обратно в enum
            } catch (e: IllegalArgumentException) {
                RaceStatus.STUCK
            }
        }
    }
}