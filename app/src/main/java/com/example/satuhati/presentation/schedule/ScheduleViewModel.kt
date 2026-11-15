package com.example.satuhati.presentation.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satuhati.data.repository.ScheduleRepositoryImpl
import com.example.satuhati.domain.model.AncSchedule
import com.example.satuhati.domain.model.VisitType
import com.example.satuhati.domain.repository.IScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class ScheduleUiState(
    val schedules: List<AncSchedule> = emptyList(),
    val selectedSchedule: AncSchedule? = null,
    val isLoading: Boolean = false,
    val isSavedSuccess: Boolean = false,
    val message: String? = null
)

class ScheduleViewModel(
    private val scheduleRepository: IScheduleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    private var currentCoupleId: String = ""

    fun loadSchedules(coupleId: String) {
        if (coupleId.isBlank()) return
        currentCoupleId = coupleId
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            scheduleRepository.getSchedules(coupleId).collect { list ->
                _uiState.update {
                    it.copy(
                        schedules = list.sortedBy { s -> s.dateEpochMillis },
                        isLoading = false
                    )
                }
            }
        }
    }

    fun addSchedule(
        coupleId: String,
        visitType: VisitType,
        title: String,
        dateEpochMillis: Long,
        location: String,
        doctorOrMidwife: String,
        notes: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }
            val newSchedule = AncSchedule(
                id = UUID.randomUUID().toString(),
                coupleId = coupleId,
                visitType = visitType,
                title = title.ifBlank { visitType.label },
                dateEpochMillis = dateEpochMillis,
                location = location,
                doctorOrMidwife = doctorOrMidwife,
                notes = notes,
                isCompleted = false,
                husbandChecklist = ScheduleRepositoryImpl.createDefaultHusbandChecklist(),
                createdAtEpochMillis = System.currentTimeMillis()
            )

            val result = scheduleRepository.saveSchedule(newSchedule)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSavedSuccess = true,
                        message = "Jadwal pemeriksaan berhasil disimpan!"
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        message = result.exceptionOrNull()?.localizedMessage ?: "Gagal menyimpan jadwal"
                    )
                }
            }
        }
    }

    fun toggleCompletion(schedule: AncSchedule) {
        viewModelScope.launch {
            scheduleRepository.toggleScheduleCompletion(
                coupleId = schedule.coupleId,
                scheduleId = schedule.id,
                isCompleted = !schedule.isCompleted
            )
        }
    }

    fun toggleHusbandChecklist(scheduleId: String, itemId: String, isChecked: Boolean) {
        if (currentCoupleId.isBlank()) return
        viewModelScope.launch {
            scheduleRepository.toggleHusbandChecklist(
                coupleId = currentCoupleId,
                scheduleId = scheduleId,
                itemId = itemId,
                isChecked = isChecked
            )
        }
    }

    fun deleteSchedule(scheduleId: String) {
        if (currentCoupleId.isBlank()) return
        viewModelScope.launch {
            scheduleRepository.deleteSchedule(currentCoupleId, scheduleId)
        }
    }

    fun selectSchedule(schedule: AncSchedule) {
        _uiState.update { it.copy(selectedSchedule = schedule) }
    }

    fun resetSavedState() {
        _uiState.update { it.copy(isSavedSuccess = false, message = null) }
    }
}
