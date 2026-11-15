package com.example.satuhati.data.repository

import com.example.satuhati.core.common.Resource
import com.example.satuhati.data.local.dao.NoteDao
import com.example.satuhati.data.local.entity.PregnancyNoteEntity
import com.example.satuhati.data.remote.service.FirestoreNoteService
import com.example.satuhati.domain.model.PregnancyNote
import com.example.satuhati.domain.repository.INoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class NoteRepositoryImpl(
    private val firestoreService: FirestoreNoteService,
    private val noteDao: NoteDao,
    private val externalScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : INoteRepository {

    private var activeCoupleId: String? = null

    override fun getNotes(coupleId: String): Flow<List<PregnancyNote>> {
        if (activeCoupleId != coupleId) {
            activeCoupleId = coupleId
            // Sync from remote Firestore to Room database in background
            firestoreService.observeNotes(coupleId)
                .onEach { remoteNotes ->
                    withContext(Dispatchers.IO) {
                        noteDao.insertNotes(remoteNotes.map { PregnancyNoteEntity.fromDomain(it) })
                    }
                }
                .launchIn(externalScope)
        }

        // Return local Room database Flow (Single Source of Truth)
        return noteDao.getNotesByCouple(coupleId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun addNote(note: PregnancyNote): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            // Save to local Room first (Immediate UI response)
            withContext(Dispatchers.IO) {
                noteDao.insertNote(PregnancyNoteEntity.fromDomain(note))
            }
            // Push to Firebase Cloud
            firestoreService.saveNote(note)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Gagal menyimpan catatan."))
        }
    }

    override fun markNotesAsRead(coupleId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            withContext(Dispatchers.IO) {
                noteDao.markAllAsRead(coupleId)
            }
            firestoreService.markAllNotesAsRead(coupleId)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Gagal memperbarui status terbaca."))
        }
    }

    override fun respondToNote(
        coupleId: String,
        noteId: String,
        response: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val now = System.currentTimeMillis()
            withContext(Dispatchers.IO) {
                noteDao.updateHusbandResponse(noteId, response, now)
            }
            firestoreService.respondToNote(coupleId, noteId, response)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Gagal mengirimkan tanggapan."))
        }
    }
}
