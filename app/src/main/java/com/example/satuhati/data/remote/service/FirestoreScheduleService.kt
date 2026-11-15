package com.example.satuhati.data.remote.service

import com.example.satuhati.domain.model.AncSchedule
import com.example.satuhati.domain.model.HusbandChecklistItem
import com.example.satuhati.domain.model.VisitType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreScheduleService(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private fun schedulesCollection(coupleId: String) =
        firestore.collection("couples").document(coupleId).collection("schedules")

    suspend fun saveSchedule(schedule: AncSchedule) {
        val checklistMapList = schedule.husbandChecklist.map { item ->
            mapOf(
                "id" to item.id,
                "title" to item.title,
                "isChecked" to item.isChecked
            )
        }

        val map = hashMapOf(
            "id" to schedule.id,
            "coupleId" to schedule.coupleId,
            "visitType" to schedule.visitType.name,
            "title" to schedule.title,
            "dateEpochMillis" to schedule.dateEpochMillis,
            "location" to schedule.location,
            "doctorOrMidwife" to schedule.doctorOrMidwife,
            "notes" to schedule.notes,
            "isCompleted" to schedule.isCompleted,
            "husbandChecklist" to checklistMapList,
            "createdAtEpochMillis" to schedule.createdAtEpochMillis
        )
        schedulesCollection(schedule.coupleId).document(schedule.id).set(map).await()
    }

    suspend fun updateCompletion(coupleId: String, scheduleId: String, isCompleted: Boolean) {
        schedulesCollection(coupleId).document(scheduleId).update("isCompleted", isCompleted).await()
    }

    suspend fun updateChecklist(coupleId: String, scheduleId: String, checklist: List<HusbandChecklistItem>) {
        val checklistMapList = checklist.map { item ->
            mapOf(
                "id" to item.id,
                "title" to item.title,
                "isChecked" to item.isChecked
            )
        }
        schedulesCollection(coupleId).document(scheduleId).update("husbandChecklist", checklistMapList).await()
    }

    suspend fun deleteSchedule(coupleId: String, scheduleId: String) {
        schedulesCollection(coupleId).document(scheduleId).delete().await()
    }

    fun observeSchedules(coupleId: String): Flow<List<AncSchedule>> = callbackFlow {
        val listener: ListenerRegistration = schedulesCollection(coupleId)
            .orderBy("dateEpochMillis", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val schedules = snapshot.documents.mapNotNull { doc ->
                        val rawChecklist = doc.get("husbandChecklist") as? List<Map<String, Any>> ?: emptyList()
                        val checklistItems = rawChecklist.map { itemMap ->
                            HusbandChecklistItem(
                                id = itemMap["id"] as? String ?: "",
                                title = itemMap["title"] as? String ?: "",
                                isChecked = itemMap["isChecked"] as? Boolean ?: false
                            )
                        }

                        val visitTypeName = doc.getString("visitType") ?: VisitType.K1.name
                        val vType = try {
                            VisitType.valueOf(visitTypeName)
                        } catch (_: Exception) {
                            VisitType.K1
                        }

                        AncSchedule(
                            id = doc.getString("id") ?: doc.id,
                            coupleId = doc.getString("coupleId") ?: coupleId,
                            visitType = vType,
                            title = doc.getString("title") ?: "",
                            dateEpochMillis = doc.getLong("dateEpochMillis") ?: System.currentTimeMillis(),
                            location = doc.getString("location") ?: "",
                            doctorOrMidwife = doc.getString("doctorOrMidwife") ?: "",
                            notes = doc.getString("notes") ?: "",
                            isCompleted = doc.getBoolean("isCompleted") ?: false,
                            husbandChecklist = checklistItems,
                            createdAtEpochMillis = doc.getLong("createdAtEpochMillis") ?: System.currentTimeMillis()
                        )
                    }
                    trySend(schedules)
                } else {
                    trySend(emptyList())
                }
            }
        awaitClose { listener.remove() }
    }
}
