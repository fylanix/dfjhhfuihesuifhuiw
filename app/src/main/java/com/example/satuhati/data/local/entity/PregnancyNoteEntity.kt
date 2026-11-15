package com.example.satuhati.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.satuhati.domain.model.PregnancyNote

@Entity(tableName = "pregnancy_notes")
data class PregnancyNoteEntity(
    @PrimaryKey
    val id: String,
    val coupleId: String,
    val authorId: String,
    val authorName: String,
    val dateMillis: Long,
    val mood: String,
    val category: String,
    val content: String,
    val isReadByHusband: Boolean,
    val husbandResponse: String?,
    val husbandRespondedAt: Long?
) {
    fun toDomain(): PregnancyNote {
        return PregnancyNote(
            id = id,
            coupleId = coupleId,
            authorId = authorId,
            authorName = authorName,
            dateMillis = dateMillis,
            mood = mood,
            category = category,
            content = content,
            isReadByHusband = isReadByHusband,
            husbandResponse = husbandResponse,
            husbandRespondedAt = husbandRespondedAt
        )
    }

    companion object {
        fun fromDomain(note: PregnancyNote): PregnancyNoteEntity {
            return PregnancyNoteEntity(
                id = note.id,
                coupleId = note.coupleId,
                authorId = note.authorId,
                authorName = note.authorName,
                dateMillis = note.dateMillis,
                mood = note.mood,
                category = note.category,
                content = note.content,
                isReadByHusband = note.isReadByHusband,
                husbandResponse = note.husbandResponse,
                husbandRespondedAt = note.husbandRespondedAt
            )
        }
    }
}
