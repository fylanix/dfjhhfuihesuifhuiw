package com.example.satuhati.domain.repository

import com.example.satuhati.domain.model.AncSchedule
import kotlinx.coroutines.flow.Flow

interface IScheduleRepository {
    fun getSchedules(coupleId: String): Flow<List<AncSchedule>>
    suspend fun saveSchedule(schedule: AncSchedule): Result<Unit>
    suspend fun toggleScheduleCompletion(coupleId: String, scheduleId: String, isCompleted: Boolean): Result<Unit>
    suspend fun toggleHusbandChecklist(coupleId: String, scheduleId: String, itemId: String, isChecked: Boolean): Result<Unit>
    suspend fun deleteSchedule(coupleId: String, scheduleId: String): Result<Unit>
}
