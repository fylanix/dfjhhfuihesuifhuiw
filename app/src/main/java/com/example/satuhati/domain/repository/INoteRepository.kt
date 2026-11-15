package com.example.satuhati.domain.repository

import com.example.satuhati.core.common.Resource
import com.example.satuhati.domain.model.PregnancyNote
import kotlinx.coroutines.flow.Flow

interface INoteRepository {
    fun getNotes(coupleId: String): Flow<List<PregnancyNote>>
    fun addNote(note: PregnancyNote): Flow<Resource<Unit>>
    fun markNotesAsRead(coupleId: String): Flow<Resource<Unit>>
    fun respondToNote(coupleId: String, noteId: String, response: String): Flow<Resource<Unit>>
}
