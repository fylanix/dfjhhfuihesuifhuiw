package com.example.satuhati.domain.model

data class QuizQuestion(
    val id: String = "",
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctOptionIndex: Int = 0,
    val clinicalWarningTitle: String = "",
    val clinicalExplanation: String = "",
    val coupleSharingPrompt: String = ""
)

data class QuizResult(
    val moduleId: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val correctCount: Int = 0,
    val isPassed: Boolean = false,
    val achievementBadge: String = "Ayah/Bunda Siaga",
    val recommendation: String = ""
)
