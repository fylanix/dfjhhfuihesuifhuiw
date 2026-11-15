package com.example.satuhati.data.repository

import com.example.satuhati.data.local.dao.ScheduleDao
import com.example.satuhati.data.local.entity.AncScheduleEntity
import com.example.satuhati.data.remote.service.FirestoreScheduleService
import com.example.satuhati.domain.model.AncSchedule
import com.example.satuhati.domain.model.HusbandChecklistItem
import com.example.satuhati.domain.model.VisitType
import com.example.satuhati.domain.repository.IScheduleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.util.UUID

class ScheduleRepositoryImpl(
    private val firestoreService: FirestoreScheduleService,
    private val scheduleDao: ScheduleDao,
    private val externalScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : IScheduleRepository {

    private var activeCoupleId: String? = null

    override fun getSchedules(coupleId: String): Flow<List<AncSchedule>> {
        if (activeCoupleId != coupleId) {
            activeCoupleId = coupleId
            // Sync from remote Firestore to Room database in background
            firestoreService.observeSchedules(coupleId)
                .onEach { remoteSchedules ->
                    withContext(Dispatchers.IO) {
                        if (remoteSchedules.isNotEmpty()) {
                            scheduleDao.insertSchedules(remoteSchedules.map { AncScheduleEntity.fromDomain(it) })
                        }
                    }
                }
                .launchIn(externalScope)
        }

        return scheduleDao.getSchedulesFlow(coupleId).map { entities ->
            if (entities.isEmpty()) {
                // If local database has no schedules yet, provide default Kemenkes K1-K6 template
                val defaults = generateDefaultKemenkesSchedules(coupleId)
                withContext(Dispatchers.IO) {
                    scheduleDao.insertSchedules(defaults.map { AncScheduleEntity.fromDomain(it) })
                }
                defaults
            } else {
                entities.map { it.toDomain() }
            }
        }
    }

    override suspend fun saveSchedule(schedule: AncSchedule): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val finalSchedule = if (schedule.id.isBlank()) {
                schedule.copy(
                    id = UUID.randomUUID().toString(),
                    husbandChecklist = if (schedule.husbandChecklist.isEmpty()) createDefaultHusbandChecklist() else schedule.husbandChecklist
                )
            } else {
                schedule
            }

            // Save to Room immediately
            scheduleDao.insertSchedule(AncScheduleEntity.fromDomain(finalSchedule))

            // Push to Firebase Firestore
            firestoreService.saveSchedule(finalSchedule)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleScheduleCompletion(
        coupleId: String,
        scheduleId: String,
        isCompleted: Boolean
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            scheduleDao.updateCompletionStatus(scheduleId, isCompleted)
            firestoreService.updateCompletion(coupleId, scheduleId, isCompleted)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleHusbandChecklist(
        coupleId: String,
        scheduleId: String,
        itemId: String,
        isChecked: Boolean
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val entity = scheduleDao.getScheduleById(scheduleId)
            if (entity != null) {
                val currentSchedule = entity.toDomain()
                val updatedChecklist = currentSchedule.husbandChecklist.map { item ->
                    if (item.id == itemId) item.copy(isChecked = isChecked) else item
                }
                val updatedSchedule = currentSchedule.copy(husbandChecklist = updatedChecklist)
                scheduleDao.insertSchedule(AncScheduleEntity.fromDomain(updatedSchedule))
                firestoreService.updateChecklist(coupleId, scheduleId, updatedChecklist)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteSchedule(coupleId: String, scheduleId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            scheduleDao.deleteScheduleById(scheduleId)
            firestoreService.deleteSchedule(coupleId, scheduleId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        fun createDefaultHusbandChecklist(): List<HusbandChecklistItem> {
            return listOf(
                HusbandChecklistItem("kia", "Buku KIA (Kesehatan Ibu dan Anak)", false),
                HusbandChecklistItem("bpjs", "Kartu BPJS / Asuransi & Surat Rujukan", false),
                HusbandChecklistItem("ktp", "KTP Suami dan KTP Istri", false),
                HusbandChecklistItem("transport", "Transportasi & Rute ke Fasilitas Kesehatan", false),
                HusbandChecklistItem("questions", "Daftar Keluhan & Pertanyaan untuk Dokter/Bidan", false),
                HusbandChecklistItem("drink", "Air Minum & Camilan Nutrisi Ibu Hamil", false)
            )
        }

        fun generateDefaultKemenkesSchedules(coupleId: String): List<AncSchedule> {
            val now = System.currentTimeMillis()
            val dayMillis = 86400000L

            return listOf(
                AncSchedule(
                    id = "default_k1_${coupleId}",
                    coupleId = coupleId,
                    visitType = VisitType.K1,
                    title = "Kunjungan K1 (Trimester 1)",
                    dateEpochMillis = now + 5 * dayMillis,
                    location = "Puskesmas / RS Rujukan",
                    doctorOrMidwife = "Dokter Sp.OG / Bidan",
                    notes = "Skrining USG Trimester 1, cek lab Hb, golongan darah, dan edukasi asam folat.",
                    isCompleted = false,
                    husbandChecklist = createDefaultHusbandChecklist(),
                    createdAtEpochMillis = now
                ),
                AncSchedule(
                    id = "default_k2_${coupleId}",
                    coupleId = coupleId,
                    visitType = VisitType.K2,
                    title = "Kunjungan K2 (Trimester 2)",
                    dateEpochMillis = now + 35 * dayMillis,
                    location = "Puskesmas / Klinik Pratama",
                    doctorOrMidwife = "Bidan Desa / Dokter",
                    notes = "Pemeriksaan tensi darah, penimbangan berat badan, lingkar lengan atas (LiLA), dan tablet Fe.",
                    isCompleted = false,
                    husbandChecklist = createDefaultHusbandChecklist(),
                    createdAtEpochMillis = now
                ),
                AncSchedule(
                    id = "default_k3_${coupleId}",
                    coupleId = coupleId,
                    visitType = VisitType.K3,
                    title = "Kunjungan K3 (Trimester 2)",
                    dateEpochMillis = now + 65 * dayMillis,
                    location = "Puskesmas / Klinik",
                    doctorOrMidwife = "Bidan / Dokter",
                    notes = "Evaluasi tumbuh kembang janin, denyut jantung janin (DJJ), dan pemantauan gerakan.",
                    isCompleted = false,
                    husbandChecklist = createDefaultHusbandChecklist(),
                    createdAtEpochMillis = now
                ),
                AncSchedule(
                    id = "default_k4_${coupleId}",
                    coupleId = coupleId,
                    visitType = VisitType.K4,
                    title = "Kunjungan K4 (Trimester 3)",
                    dateEpochMillis = now + 95 * dayMillis,
                    location = "Puskesmas / RS",
                    doctorOrMidwife = "Bidan / Dokter Sp.OG",
                    notes = "Skrining preeklamsia trimester 3, cek lab protein urine, dan kesiapan persalinan P4K.",
                    isCompleted = false,
                    husbandChecklist = createDefaultHusbandChecklist(),
                    createdAtEpochMillis = now
                ),
                AncSchedule(
                    id = "default_k5_${coupleId}",
                    coupleId = coupleId,
                    visitType = VisitType.K5,
                    title = "Kunjungan K5 (Trimester 3)",
                    dateEpochMillis = now + 115 * dayMillis,
                    location = "RS / Klinik Spesialis",
                    doctorOrMidwife = "Dokter Sp.OG",
                    notes = "Pemeriksaan USG Trimester 3: posisi janin, letak plasenta, dan kecukupan air ketuban.",
                    isCompleted = false,
                    husbandChecklist = createDefaultHusbandChecklist(),
                    createdAtEpochMillis = now
                ),
                AncSchedule(
                    id = "default_k6_${coupleId}",
                    coupleId = coupleId,
                    visitType = VisitType.K6,
                    title = "Kunjungan K6 (Menjelang Persalinan)",
                    dateEpochMillis = now + 130 * dayMillis,
                    location = "Fasilitas Tempat Bersalin",
                    doctorOrMidwife = "Dokter Sp.OG / Tim Bidan",
                    notes = "Finalisasi rencana persalinan, persiapan donor darah siaga, dan tanda-tanda persalinan.",
                    isCompleted = false,
                    husbandChecklist = createDefaultHusbandChecklist(),
                    createdAtEpochMillis = now
                )
            )
        }
    }
}
