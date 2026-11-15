package com.example.satuhati.domain.model

data class LearningModule(
    val id: String = "",
    val trimester: Int = 1, // 1: Trimester 1, 2: Trimester 2, 3: Trimester 3, 4: Pasca Salin
    val title: String = "",
    val summary: String = "",
    val estimatedMinutes: Int = 5,
    val heroImageUrl: String? = null,
    val contentSections: List<String> = emptyList(),
    val husbandTips: String = "",
    val isRead: Boolean = false,
    val questions: List<QuizQuestion> = emptyList()
)
