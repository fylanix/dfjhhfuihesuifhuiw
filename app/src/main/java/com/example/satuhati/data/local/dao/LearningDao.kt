package com.example.satuhati.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.satuhati.data.local.entity.LearningModuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LearningDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModules(modules: List<LearningModuleEntity>): List<Long>

    @Query("SELECT * FROM learning_modules ORDER BY trimester ASC")
    fun getAllModules(): Flow<List<LearningModuleEntity>>

    @Query("SELECT * FROM learning_modules WHERE id = :id LIMIT 1")
    fun getModuleById(id: String): Flow<LearningModuleEntity?>

    @Query("UPDATE learning_modules SET isRead = 1 WHERE id = :id")
    fun markAsRead(id: String): Int
}
