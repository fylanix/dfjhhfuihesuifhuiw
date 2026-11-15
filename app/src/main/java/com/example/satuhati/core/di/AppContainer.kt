package com.example.satuhati.core.di

import android.content.Context
import com.example.satuhati.data.local.SatuHatiDatabase
import com.example.satuhati.data.remote.service.FirebaseAuthService
import com.example.satuhati.data.remote.service.FirestoreCoupleService
import com.example.satuhati.data.remote.service.FirestoreNoteService
import com.example.satuhati.data.remote.service.FirestoreScheduleService
import com.example.satuhati.data.repository.AuthRepositoryImpl
import com.example.satuhati.data.repository.LearningRepositoryImpl
import com.example.satuhati.data.repository.NoteRepositoryImpl
import com.example.satuhati.data.repository.ScheduleRepositoryImpl
import com.example.satuhati.domain.repository.IAuthRepository
import com.example.satuhati.domain.repository.ILearningRepository
import com.example.satuhati.domain.repository.INoteRepository
import com.example.satuhati.domain.repository.IScheduleRepository

class AppContainer(context: Context) {
    val database: SatuHatiDatabase by lazy {
        SatuHatiDatabase.getInstance(context)
    }

    val firebaseAuthService: FirebaseAuthService by lazy {
        FirebaseAuthService()
    }

    val firestoreCoupleService: FirestoreCoupleService by lazy {
        FirestoreCoupleService()
    }

    val firestoreNoteService: FirestoreNoteService by lazy {
        FirestoreNoteService()
    }

    val firestoreScheduleService: FirestoreScheduleService by lazy {
        FirestoreScheduleService()
    }

    val authRepository: IAuthRepository by lazy {
        AuthRepositoryImpl(
            authService = firebaseAuthService,
            firestoreService = firestoreCoupleService,
            userDao = database.userDao(),
            coupleDao = database.coupleDao()
        )
    }

    val noteRepository: INoteRepository by lazy {
        NoteRepositoryImpl(
            firestoreService = firestoreNoteService,
            noteDao = database.noteDao()
        )
    }

    val learningRepository: ILearningRepository by lazy {
        LearningRepositoryImpl(
            learningDao = database.learningDao()
        )
    }

    val scheduleRepository: IScheduleRepository by lazy {
        ScheduleRepositoryImpl(
            firestoreService = firestoreScheduleService,
            scheduleDao = database.scheduleDao()
        )
    }
}
