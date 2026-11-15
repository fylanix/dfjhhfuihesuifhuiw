package com.example.satuhati.presentation.learning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satuhati.domain.model.LearningModule
import com.example.satuhati.domain.model.QuizQuestion
import com.example.satuhati.domain.model.QuizResult
import com.example.satuhati.domain.repository.ILearningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class LearningUiState(
    val modules: List<LearningModule> = emptyList(),
    val selectedTrimester: Int = 2,
    val selectedModule: LearningModule? = null,
    // Quiz state
    val currentQuestionIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isFeedbackExpanded: Boolean = false,
    val correctCount: Int = 0,
    val quizResult: QuizResult? = null
)

class LearningViewModel(
    private val learningRepository: ILearningRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LearningUiState())
    val uiState: StateFlow<LearningUiState> = _uiState.asStateFlow()

    init {
        loadModules()
    }

    private fun loadModules() {
        learningRepository.getModules()
            .onEach { list ->
                _uiState.value = _uiState.value.copy(modules = list)
            }
            .launchIn(viewModelScope)
    }

    fun selectTrimester(trimester: Int) {
        _uiState.value = _uiState.value.copy(selectedTrimester = trimester)
    }

    fun selectModule(module: LearningModule) {
        _uiState.value = _uiState.value.copy(
            selectedModule = module,
            currentQuestionIndex = 0,
            selectedOptionIndex = null,
            isFeedbackExpanded = false,
            correctCount = 0,
            quizResult = null
        )
    }

    fun markCurrentModuleAsRead() {
        val mod = _uiState.value.selectedModule ?: return
        viewModelScope.launch {
            learningRepository.markModuleAsRead(mod.id)
        }
    }

    fun selectQuizOption(optionIndex: Int) {
        val currentMod = _uiState.value.selectedModule ?: return
        val currentQ = currentMod.questions.getOrNull(_uiState.value.currentQuestionIndex) ?: return

        val isCorrect = optionIndex == currentQ.correctOptionIndex
        val updatedCorrectCount = if (isCorrect) _uiState.value.correctCount + 1 else _uiState.value.correctCount

        _uiState.value = _uiState.value.copy(
            selectedOptionIndex = optionIndex,
            isFeedbackExpanded = true,
            correctCount = updatedCorrectCount
        )
    }

    fun nextQuestion() {
        val currentMod = _uiState.value.selectedModule ?: return
        val nextIdx = _uiState.value.currentQuestionIndex + 1

        if (nextIdx < currentMod.questions.size) {
            _uiState.value = _uiState.value.copy(
                currentQuestionIndex = nextIdx,
                selectedOptionIndex = null,
                isFeedbackExpanded = false
            )
        } else {
            // Evaluate Quiz
            val total = currentMod.questions.size
            val correct = _uiState.value.correctCount
            val score = if (total > 0) (correct * 100) / total else 0
            val isPassed = score >= 70

            val result = QuizResult(
                moduleId = currentMod.id,
                score = score,
                totalQuestions = total,
                correctCount = correct,
                isPassed = isPassed,
                achievementBadge = if (isPassed) "Orang Tua Siaga & Tanggap" else "Perlu Penguatan Materi",
                recommendation = if (isPassed)
                    "Pemahaman Anda sangat baik. Tetap pantau tanda bahaya dan komunikasikan setiap perubahan kondisi bersama pasangan."
                else
                    "Disarankan untuk membaca ulang modul dan menanyakan hal-hal yang belum dipahami saat jadwal konsultasi ANC."
            )

            viewModelScope.launch {
                learningRepository.saveQuizResult(result)
            }

            _uiState.value = _uiState.value.copy(quizResult = result)
        }
    }
}
