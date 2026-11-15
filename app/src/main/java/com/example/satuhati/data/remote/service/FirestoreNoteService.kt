package com.example.satuhati.data.remote.service

import com.example.satuhati.domain.model.PregnancyNote
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreNoteService(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private fun notesCollection(coupleId: String) =
        firestore.collection("couples").document(coupleId).collection("notes")

    suspend fun saveNote(note: PregnancyNote) {
        val map = hashMapOf(
            "id" to note.id,
            "coupleId" to note.coupleId,
            "authorId" to note.authorId,
            "authorName" to note.authorName,
            "dateMillis" to note.dateMillis,
            "mood" to note.mood,
            "category" to note.category,
            "content" to note.content,
            "isReadByHusband" to note.isReadByHusband,
            "husbandResponse" to note.husbandResponse,
            "husbandRespondedAt" to note.husbandRespondedAt
        )
        notesCollection(note.coupleId).document(note.id).set(map).await()
    }

    suspend fun markAllNotesAsRead(coupleId: String) {
        val snapshot = notesCollection(coupleId).whereEqualTo("isReadByHusband", false).get().await()
        val batch = firestore.batch()
        for (doc in snapshot.documents) {
            batch.update(doc.reference, "isReadByHusband", true)
        }
        batch.commit().await()
    }

    suspend fun respondToNote(coupleId: String, noteId: String, response: String) {
        notesCollection(coupleId).document(noteId).update(
            mapOf(
                "husbandResponse" to response,
                "husbandRespondedAt" to System.currentTimeMillis(),
                "isReadByHusband" to true
            )
        ).await()
    }

    fun observeNotes(coupleId: String): Flow<List<PregnancyNote>> = callbackFlow {
        val listener: ListenerRegistration = notesCollection(coupleId)
            .orderBy("dateMillis", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val notes = snapshot.documents.mapNotNull { doc ->
                        PregnancyNote(
                            id = doc.getString("id") ?: doc.id,
                            coupleId = doc.getString("coupleId") ?: coupleId,
                            authorId = doc.getString("authorId") ?: "",
                            authorName = doc.getString("authorName") ?: "",
                            dateMillis = doc.getLong("dateMillis") ?: System.currentTimeMillis(),
                            mood = doc.getString("mood") ?: "Senang",
                            category = doc.getString("category") ?: "Catatan Kehamilan",
                            content = doc.getString("content") ?: "",
                            isReadByHusband = doc.getBoolean("isReadByHusband") ?: false,
                            husbandResponse = doc.getString("husbandResponse"),
                            husbandRespondedAt = doc.getLong("husbandRespondedAt")
                        )
                    }
                    trySend(notes)
                } else {
                    trySend(emptyList())
                }
            }
        awaitClose { listener.remove() }
    }
}
