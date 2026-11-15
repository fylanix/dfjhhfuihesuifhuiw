package com.example.satuhati.domain.repository

import com.example.satuhati.domain.model.LearningModule
import com.example.satuhati.domain.model.QuizResult
import kotlinx.coroutines.flow.Flow

interface ILearningRepository {
    fun getModules(): Flow<List<LearningModule>>
    fun getModuleById(id: String): Flow<LearningModule?>
    suspend fun markModuleAsRead(id: String)
    suspend fun saveQuizResult(result: QuizResult)
}
