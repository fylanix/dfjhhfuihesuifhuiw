package com.example.satuhati.data.repository

import com.example.satuhati.data.local.dao.LearningDao
import com.example.satuhati.data.local.entity.LearningModuleEntity
import com.example.satuhati.domain.model.LearningModule
import com.example.satuhati.domain.model.QuizQuestion
import com.example.satuhati.domain.model.QuizResult
import com.example.satuhati.domain.repository.ILearningRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LearningRepositoryImpl(
    private val learningDao: LearningDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : ILearningRepository {

    private val staticModules = listOf(
        LearningModule(
            id = "mod-kaki-bengkak",
            trimester = 2,
            title = "Mengenal Kaki Bengkak pada Kehamilan",
            summary = "Pahami perbedaan edema fisiologis normal vs tanda bahaya preeklamsia dan peran suami.",
            estimatedMinutes = 5,
            contentSections = listOf(
                "Pembengkakan pada kaki (edema) adalah keluhan yang umum terjadi pada trimester kedua dan ketiga akibat peningkatan volume cairan tubuh serta tekanan rahim pada pembuluh darah balik.",
                "Perbedaan Tanda Normal vs Bahaya:\n• Normal: Bengkak muncul sore hari setelah berdiri/beraktivitas lama dan mereda saat kaki ditinggikan saat istirahat.\n• Bahaya (Preeklamsia): Bengkak terjadi mendadak pada wajah dan jari tangan, disertai sakit kepala hebat, pandangan kabur, atau nyeri ulu hati.",
                "Pemeriksaan Tekanan Darah & Proteinuria:\nBila bengkak tidak berkurang atau disertai tekanan darah di atas 140/90 mmHg, segera periksakan ke dokter/bidan di faskes terdekat."
            ),
            husbandTips = "Dukung Bunda dengan menyiapkan bantal untuk menyangga kaki saat tidur, membatasi konsumsi garam berlebih, dan mendampingi pemeriksaan ANC.",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    question = "Kapan kaki bengkak pada ibu hamil memerlukan pemeriksaan medis segera?",
                    options = listOf(
                        "Hanya saat bengkak muncul setelah berjalan jauh di siang hari",
                        "Bengkak tiba-tiba di wajah/tangan disertai sakit kepala hebat & pandangan kabur",
                        "Bengkak berkurang setelah istirahat dengan kaki ditinggikan",
                        "Bengkak ringan di pergelangan kaki pada sore hari"
                    ),
                    correctOptionIndex = 1,
                    clinicalWarningTitle = "Preeklamsia berat (impending eklampsia)",
                    clinicalExplanation = "Bengkak mendadak yang melibatkan wajah dan jari tangan, disertai gejala neurologis (sakit kepala berdenyut hebat dan gangguan penglihatan), merupakan tanda bahaya preeklamsia berat yang membutuhkan penanganan emergensi di fasilitas kesehatan.",
                    coupleSharingPrompt = "Diskusikan dengan pasangan mengenai nomor kontak faskes atau bidan terdekat untuk situasi darurat."
                ),
                QuizQuestion(
                    id = "q2",
                    question = "Langkah awal apa yang bisa dilakukan suami saat istri mengalami kaki bengkak fisiologis (normal)?",
                    options = listOf(
                        "Menyarankan istri berdiri lebih lama",
                        "Membantu memposisikan kaki istri lebih tinggi dari dada saat istirahat",
                        "Memberikan obat pereda nyeri tanpa resep dokter",
                        "Melarang istri minum air putih"
                    ),
                    correctOptionIndex = 1,
                    clinicalWarningTitle = "Membantu drainase cairan vena",
                    clinicalExplanation = "Meninggikan kaki dengan bantal saat berbaring membantu aliran balik cairan vena ke jantung dan mengurangi tekanan pada ekstremitas bawah secara aman.",
                    coupleSharingPrompt = "Suami dapat membantu memijat lembut kaki istri dari arah bawah ke atas untuk relaksasi."
                )
            )
        ),
        LearningModule(
            id = "mod-gizi-trimester1",
            trimester = 1,
            title = "Kebutuhan Asam Folat & Nutrisi Trimester 1",
            summary = "Pentingnya asam folat 400 mcg untuk pembentukan tabung saraf janin serta mengatasi morning sickness.",
            estimatedMinutes = 4,
            contentSections = listOf(
                "Trimester pertama adalah masa krusial pembentukan organ vital janin (organogenesis).",
                "Asam folat minimal 400 mcg per hari wajib dikonsumsi untuk mencegah cacat tabung saraf (spina bifida).",
                "Tips Mual Muntah: Makan dalam porsi kecil namun sering, hindari makanan beraroma menyengat, dan konsumsi biskuit jahe atau air hangat."
            ),
            husbandTips = "Sediakan camilan sehat di samping tempat tidur Bunda dan bantu siapkan sarapan ringan sebelum Bunda beranjak dari tempat tidur.",
            questions = listOf(
                QuizQuestion(
                    id = "q-gizi-1",
                    question = "Apa fungsi utama suplementasi asam folat pada awal kehamilan?",
                    options = listOf(
                        "Mencegah cacat tabung saraf pada janin",
                        "Membuat kulit bayi menjadi lebih putih",
                        "Menghilangkan rasa mual seketika",
                        "Menaikkan berat badan ibu secara drastis"
                    ),
                    correctOptionIndex = 0,
                    clinicalWarningTitle = "Pencegahan Neural Tube Defect (NTD)",
                    clinicalExplanation = "Asam folat sangat penting dalam penutupan tabung saraf janin yang terjadi pada 28 hari pertama kehamilan.",
                    coupleSharingPrompt = "Pastikan tablet asam folat diminum teratur setiap hari di jam yang sama."
                )
            )
        ),
        LearningModule(
            id = "mod-tanda-persalinan",
            trimester = 3,
            title = "Tanda Pasti Persalinan & Persiapan Tas Bersalin",
            summary = "Kenali kontraksi asli vs palsu (Braxton Hicks) dan persiapan perlengkapan ke rumah sakit.",
            estimatedMinutes = 6,
            contentSections = listOf(
                "Kontraksi persalinan asli terasa teratur, semakin lama semakin sering dan semakin kuat, serta tidak hilang saat beristirahat.",
                "Keluarnya lendir bercampur darah (bloody show) dan pecahnya ketuban adalah tanda persalinan aktif.",
                "Tas persalinan harus sudah siap di dekat pintu masuk sejak usia kehamilan 36 minggu."
            ),
            husbandTips = "Pastikan kendaraan siap siaga dengan bensin penuh, simpan nomor telepon rumah sakit/bidan, dan bawa buku KIA serta berkas BPJS/KTP.",
            questions = listOf(
                QuizQuestion(
                    id = "q-salin-1",
                    question = "Ciri utama kontraksi persalinan asli (inpartu) adalah:",
                    options = listOf(
                        "Hilang saat ibu berjalan-jalan santai",
                        "Hanya terasa di bagian perut depan saja",
                        "Interval semakin teratur, semakin rapat, dan durasi semakin panjang",
                        "Frekuensi tidak menentu dan intensitas lemah"
                    ),
                    correctOptionIndex = 2,
                    clinicalWarningTitle = "Kemajuan Persalinan Alami",
                    clinicalExplanation = "Kontraksi asli terjadi teratur dengan pola gelombang yang memperpendek interval dan menyebabkan pembukaan serviks.",
                    coupleSharingPrompt = "Gunakan stopwatch atau timer aplikasi SatuHati saat kontraksi mulai terasa setiap 5-10 menit."
                )
            )
        ),
        LearningModule(
            id = "mod-pasca-salin",
            trimester = 4,
            title = "Perawatan Masa Nifas & Menyusui (ASI Eksklusif)",
            summary = "Panduan perlekatan menyusui yang benar dan deteksi dini sindrom baby blues.",
            estimatedMinutes = 5,
            contentSections = listOf(
                "Masa nifas berlangsung selama 42 hari setelah persalinan.",
                "Inisiasi Menyusu Dini (IMD) dan pelekatan yang tepat mencegah puting lecet serta merangsang hormon prolaktin dan oksitosin.",
                "Dukungan suami adalah faktor terbesar keberhasilan menyusui dan pencegahan depresi pasca salin."
            ),
            husbandTips = "Bantu Bunda dengan mengganti popok bayi, memijat punggung Bunda (pijat oksitosin), dan memberikan kata-kata penguatan.",
            questions = listOf(
                QuizQuestion(
                    id = "q-nifas-1",
                    question = "Bagaimana peran suami yang paling efektif dalam mendukung kelancaran ASI Bunda?",
                    options = listOf(
                        "Memberikan susu formula tanpa konsultasi dokter",
                        "Membantu tugas rumah tangga dan memberikan dukungan emosional agar Bunda bahagia",
                        "Menuntut Bunda langsung ramping setelah melahirkan",
                        "Menyerahkan seluruh perawatan bayi kepada Bunda"
                    ),
                    correctOptionIndex = 1,
                    clinicalWarningTitle = "Pelepasan Hormon Oksitosin (Love Hormone)",
                    clinicalExplanation = "Hormon oksitosin yang memicu pengeluaran ASI (let-down reflex) sangat dipengaruhi oleh suasana hati Bunda yang tenang, bahagia, dan merasa didukung oleh suami.",
                    coupleSharingPrompt = "Diskusikan pembagian tugas malam hari saat bayi terbangun menyusu."
                )
            )
        )
    )

    init {
        // Seed database if empty
        scope.launch {
            val entities = staticModules.map {
                LearningModuleEntity(
                    id = it.id,
                    trimester = it.trimester,
                    title = it.title,
                    summary = it.summary,
                    estimatedMinutes = it.estimatedMinutes,
                    contentBody = it.contentSections.joinToString("\n\n"),
                    husbandTips = it.husbandTips,
                    isRead = it.isRead
                )
            }
            learningDao.insertModules(entities)
        }
    }

    override fun getModules(): Flow<List<LearningModule>> {
        return learningDao.getAllModules().map { entities ->
            if (entities.isEmpty()) {
                staticModules
            } else {
                entities.map { entity ->
                    val static = staticModules.find { it.id == entity.id }
                    LearningModule(
                        id = entity.id,
                        trimester = entity.trimester,
                        title = entity.title,
                        summary = entity.summary,
                        estimatedMinutes = entity.estimatedMinutes,
                        contentSections = static?.contentSections ?: listOf(entity.contentBody),
                        husbandTips = entity.husbandTips,
                        isRead = entity.isRead,
                        questions = static?.questions ?: emptyList()
                    )
                }
            }
        }
    }

    override fun getModuleById(id: String): Flow<LearningModule?> = flow {
        val static = staticModules.find { it.id == id }
        emit(static)
    }

    override suspend fun markModuleAsRead(id: String) {
        withContext(Dispatchers.IO) {
            learningDao.markAsRead(id)
        }
    }

    override suspend fun saveQuizResult(result: QuizResult) {
        withContext(Dispatchers.IO) {
            learningDao.markAsRead(result.moduleId)
        }
    }
}
