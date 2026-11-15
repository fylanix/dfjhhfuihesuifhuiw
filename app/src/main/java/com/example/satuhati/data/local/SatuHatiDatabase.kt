package com.example.satuhati.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.satuhati.data.local.dao.CoupleDao
import com.example.satuhati.data.local.dao.LearningDao
import com.example.satuhati.data.local.dao.NoteDao
import com.example.satuhati.data.local.dao.UserDao
import com.example.satuhati.data.local.entity.CoupleEntity
import com.example.satuhati.data.local.entity.LearningModuleEntity
import com.example.satuhati.data.local.entity.PregnancyNoteEntity
import com.example.satuhati.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        CoupleEntity::class,
        PregnancyNoteEntity::class,
        LearningModuleEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class SatuHatiDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun coupleDao(): CoupleDao
    abstract fun noteDao(): NoteDao
    abstract fun learningDao(): LearningDao

    companion object {
        @Volatile
        private var INSTANCE: SatuHatiDatabase? = null

        fun getInstance(context: Context): SatuHatiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SatuHatiDatabase::class.java,
                    "satuhati_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
