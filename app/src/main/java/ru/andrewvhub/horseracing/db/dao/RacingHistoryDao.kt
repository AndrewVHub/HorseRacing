package ru.andrewvhub.horseracing.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.andrewvhub.horseracing.db.entity.RaceResultEntity

@Dao
interface RacingHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(contact: RaceResultEntity)

    @Update
    suspend fun updateRaceResult(raceResult: RaceResultEntity)

    @Query("SELECT * FROM ${RaceResultEntity.TABLE_NAME} ORDER BY timestamp DESC")
    fun getContactAsFlow(): Flow<List<RaceResultEntity>>

    /**
     * Получает гонки, которые находятся в указанном статусе и
     * чей timestamp старше заданного порогового времени.
     *
     * @param statusName Строковое имя статуса (например, "RUNNING").
     * @param thresholdTime Порог времени в миллисекундах (System.currentTimeMillis() - 7000L).
     */
    @Query("SELECT * FROM ${RaceResultEntity.TABLE_NAME} WHERE status = :statusName AND timestamp < :thresholdTime")
    fun getRacesByStatusAndTimestampThreshold(statusName: String, thresholdTime: Long): Flow<List<RaceResultEntity>>

    @Query("delete from ${RaceResultEntity.TABLE_NAME}")
    suspend fun clear()
}