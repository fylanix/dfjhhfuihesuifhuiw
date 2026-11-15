package com.example.satuhati.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learning_modules")
data class LearningModuleEntity(
    @PrimaryKey
    val id: String,
    val trimester: Int,
    val title: String,
    val summary: String,
    val estimatedMinutes: Int,
    val contentBody: String,
    val husbandTips: String,
    val isRead: Boolean
)
