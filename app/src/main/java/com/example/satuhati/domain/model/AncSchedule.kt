package com.example.satuhati.domain.model

enum class VisitType(val label: String, val description: String) {
    K1("K1 (Trimester 1)", "Sebelum minggu ke-12 (Pemeriksaan Dokter & USG Skrining)"),
    K2("K2 (Trimester 2)", "Minggu ke-13 s.d. 24 (Pemeriksaan Bidan/Dokter)"),
    K3("K3 (Trimester 2)", "Minggu ke-24 s.d. 28 (Pemeriksaan Bidan/Dokter & Pemantauan TFU)"),
    K4("K4 (Trimester 3)", "Minggu ke-28 s.d. 32 (Pemeriksaan Bidan/Dokter & Pencegahan Anemia)"),
    K5("K5 (Trimester 3)", "Minggu ke-32 s.d. 36 (Pemeriksaan Dokter/Bidan & Skrining Letak Janin)"),
    K6("K6 (Trimester 3)", "Minggu ke-36 s.d. Persalinan (USG Trimester 3 & Rencana Persalinan)"),
    USG("Pemeriksaan USG Khusus", "Pemeriksaan USG Fetomaternal / Khusus"),
    LAB("Pemeriksaan Lab Rutin", "Pemeriksaan Darah Lengkap, Golongan Darah & Skrining Urine"),
    OTHER("Pemeriksaan Lainnya", "Konsultasi atau kontrol khusus sesuai keluhan")
}

data class HusbandChecklistItem(
    val id: String,
    val title: String,
    val isChecked: Boolean = false
)

data class AncSchedule(
    val id: String = "",
    val coupleId: String = "",
    val visitType: VisitType = VisitType.K1,
    val title: String = "",
    val dateEpochMillis: Long = System.currentTimeMillis(),
    val location: String = "",
    val doctorOrMidwife: String = "",
    val notes: String = "",
    val isCompleted: Boolean = false,
    val husbandChecklist: List<HusbandChecklistItem> = emptyList(),
    val createdAtEpochMillis: Long = System.currentTimeMillis()
)
