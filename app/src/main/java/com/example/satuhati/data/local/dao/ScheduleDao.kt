package com.example.satuhati.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.satuhati.data.local.entity.AncScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules WHERE coupleId = :coupleId ORDER BY dateEpochMillis ASC")
    fun getSchedulesFlow(coupleId: String): Flow<List<AncScheduleEntity>>

    @Query("SELECT * FROM schedules WHERE id = :id LIMIT 1")
    fun getScheduleById(id: String): AncScheduleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(schedule: AncScheduleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedules(schedules: List<AncScheduleEntity>)

    @Query("UPDATE schedules SET isCompleted = :isCompleted WHERE id = :id")
    fun updateCompletionStatus(id: String, isCompleted: Boolean): Int

    @Query("UPDATE schedules SET husbandChecklistJson = :checklistJson WHERE id = :id")
    fun updateHusbandChecklistJson(id: String, checklistJson: String): Int

    @Query("DELETE FROM schedules WHERE id = :id")
    fun deleteScheduleById(id: String): Int

    @Query("DELETE FROM schedules WHERE coupleId = :coupleId")
    fun clearSchedules(coupleId: String): Int
}
