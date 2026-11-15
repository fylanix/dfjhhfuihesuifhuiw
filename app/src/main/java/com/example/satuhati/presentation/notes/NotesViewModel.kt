package com.example.satuhati.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satuhati.core.common.Resource
import com.example.satuhati.domain.model.PregnancyNote
import com.example.satuhati.domain.model.User
import com.example.satuhati.domain.repository.INoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.UUID

data class NotesUiState(
    val notes: List<PregnancyNote> = emptyList(),
    val isLoading: Boolean = false,
    val isSavingSuccess: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class NotesViewModel(
    private val noteRepository: INoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    fun loadNotes(coupleId: String) {
        if (coupleId.isBlank()) return
        noteRepository.getNotes(coupleId)
            .onEach { noteList ->
                _uiState.value = _uiState.value.copy(
                    notes = noteList,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

    fun addNote(coupleId: String, currentUser: User, mood: String, content: String) {
        if (coupleId.isBlank() || content.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Catatan tidak boleh kosong.")
            return
        }

        val note = PregnancyNote(
            id = UUID.randomUUID().toString(),
            coupleId = coupleId,
            authorId = currentUser.id,
            authorName = currentUser.name,
            dateMillis = System.currentTimeMillis(),
            mood = mood.ifBlank { "Senang" },
            category = "Catatan Kehamilan",
            content = content,
            isReadByHusband = false
        )

        noteRepository.addNote(note)
            .onEach { res ->
                when (res) {
                    is Resource.Loading -> _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSavingSuccess = true,
                            successMessage = "Catatan berhasil disimpan."
                        )
                    }
                    is Resource.Error -> _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = res.message)
                }
            }
            .launchIn(viewModelScope)
    }

    fun markAllAsRead(coupleId: String) {
        if (coupleId.isBlank()) return
        noteRepository.markNotesAsRead(coupleId).launchIn(viewModelScope)
    }

    fun respondToNote(coupleId: String, noteId: String, response: String) {
        if (response.isBlank()) return
        noteRepository.respondToNote(coupleId, noteId, response)
            .onEach { res ->
                when (res) {
                    is Resource.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                    is Resource.Success -> _uiState.value = _uiState.value.copy(isLoading = false, successMessage = "Tanggapan berhasil dikirim.")
                    is Resource.Error -> _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = res.message)
                }
            }
            .launchIn(viewModelScope)
    }

    fun resetSavingState() {
        _uiState.value = _uiState.value.copy(isSavingSuccess = false, errorMessage = null, successMessage = null)
    }
}
