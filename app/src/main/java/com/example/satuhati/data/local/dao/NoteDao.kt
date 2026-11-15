package com.example.satuhati.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.satuhati.data.local.entity.PregnancyNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: PregnancyNoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(notes: List<PregnancyNoteEntity>): List<Long>

    @Query("SELECT * FROM pregnancy_notes WHERE coupleId = :coupleId ORDER BY dateMillis DESC")
    fun getNotesByCouple(coupleId: String): Flow<List<PregnancyNoteEntity>>

    @Query("UPDATE pregnancy_notes SET isReadByHusband = 1 WHERE coupleId = :coupleId")
    fun markAllAsRead(coupleId: String): Int

    @Query("UPDATE pregnancy_notes SET husbandResponse = :response, husbandRespondedAt = :respondedAt WHERE id = :noteId")
    fun updateHusbandResponse(noteId: String, response: String, respondedAt: Long): Int
}
