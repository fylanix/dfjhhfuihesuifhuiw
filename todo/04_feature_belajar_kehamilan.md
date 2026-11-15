# Perencanaan Fitur 3: Belajar Kehamilan (`feature/belajar-kehamilan`)

Fitur ini menyediakan kurikulum literasi medis kehamilan berbasis trimester, modul bacaan terstruktur, kuis interaktif dengan umpan balik klinis instan (*instant clinical feedback*), dan evaluasi pemahaman untuk calon ibu dan ayah.

---

## 1. Identifikasi Frame Figma
Berdasarkan section Figma `13:1316` (**Belajar Kehamilan - Farid Ramdhani Aziz**):
- `8:925` / `100:1986` — **Katalog Belajar Kehamilan**:
  - Tab navigasi per trimester (Trimester 1, Trimester 2, Trimester 3, Pasca Salin).
  - Kartu modul artikel (Judul, ringkasan, estimasi menit baca, status baca/kuis).
  - Banner motivasi belajar bersama pasangan.
- `13:1007` / `100:2291` — **Modul Bacaan**:
  - Artikel medis komprehensif, daftar poin penting, tips peran suami.
  - Tombol CTA di bagian akhir: "Mulai Kuis Pemahaman".
- `8:922` / `100:2514` — **Kuis Pemahaman - Kaki Bengkak**:
  - Header progres kuis (Segmented progress bar).
  - Badge kemitraan pasangan (*Role Partnership Badge & Warm Prompt*).
  - Kartu pertanyaan interaktif dengan 4 pilihan ganda (Option A, B, C, D).
  - Indikator visual jawaban terpilih dan jawaban benar.
  - **Instant Clinical Feedback Accordion**: Kartu penjelasan medis langsung yang menjelaskan patofisiologi/alasan klinis jawaban (contoh: deteksi dini preeklamsia berat).
  - Tombol ajakan diskusi bersama pasangan (*Couple Interactive Sharing Prompt*).
- `8:923` / `100:2690` — **Hasil Evaluasi Kuis**:
  - Tampilan skor akhir dan badge penghargaan.
  - Rangkuman poin kunci yang telah dipelajari.
  - Rekomendasi tindakan klinis atau bahan diskusi dengan dokter saat pemeriksaan ANC.

---

## 2. Struktur Komponen & Arsitektur Fitur

### A. Domain Layer
- **Model**: `LearningModule`, `TrimesterCategory`, `QuizQuestion`, `QuizOption`, `QuizResult`, `ClinicalExplanation`
- **Use Cases**:
  - `GetModulesByTrimesterUseCase(trimester): Flow<List<LearningModule>>`
  - `GetModuleDetailUseCase(moduleId): Flow<Resource<LearningModule>>`
  - `MarkModuleAsReadUseCase(moduleId): Flow<Unit>`
  - `GetQuizByModuleUseCase(moduleId): Flow<List<QuizQuestion>>`
  - `SubmitQuizAnswersUseCase(moduleId, answers): Flow<QuizResult>`
  - `GetQuizHistoryUseCase(userId): Flow<List<QuizResult>>`

### B. Data Layer
- **Room Database (`LearningDao`)**:
  - Pre-populated modul edukasi kehamilan terverifikasi klinis.
  - Menyimpan status baca dan skor kuis lokal di tabel `learning_modules` dan `quiz_results`.
- **Firebase Firestore Learning Sync**:
  - Menyinkronkan daftar bacaan dan hasil kuis ke cloud profil pengguna/pasangan.
  - Memungkinkan konten edukasi diperbarui secara dinamis dari remote database.

### C. Presentation Layer (Jetpack Compose)
- `LearningCatalogScreen.kt`: Tab kategori trimester, search bar, list modul pembelajaran dengan kartu Material3.
- `ModuleDetailScreen.kt`: Tampilan bacaan dengan tipografi nyaman dibaca, callout box tips suami, tombol navigasi ke kuis.
- `QuizScreen.kt`:
  - `QuizProgressBar`: Indikator progres soal.
  - `QuestionCard`: Teks skenario klinis.
  - `QuizOptionItem`: Kartu opsi yang merespons state normal, selected, dan evaluated.
  - `ClinicalFeedbackAccordion`: Card accordion yang membuka penjelasan medis mendalam saat opsi dijawab.
- `QuizResultScreen.kt`: Skor persentase visual, lencana apresiasi, tombol review materi atau kembali ke katalog.
- `LearningViewModel.kt`: Mengatur state modul aktif, index pertanyaan kuis, kalkulasi skor, dan ekspansi accordion feedback.

---

## 3. Alur Kerja Git Branch
- **Nama Branch**: `feature/belajar-kehamilan`
- **Langkah Pembuatan**:
  ```bash
  git checkout -b feature/belajar-kehamilan
  ```
- **Rangkaian Semantic Commit**:
  1. `feat: buat model domain, entitas room, dan mock data modul belajar kehamilan`
  2. `feat: buat katalog belajar dengan filter trimester dan status modul`
  3. `feat: buat layar detail modul bacaan kehamilan beserta tips peran suami`
  4. `feat: implementasi kuis interaktif dengan segmented progress bar dan opsi pilihan ganda`
  5. `feat: implementasi accordion umpan balik klinis instan dan evaluasi skor`
  6. `feat: buat layar hasil evaluasi kuis dengan badge pencapaian dan rekomendasi klinis`
- **Penyelesaian**:
  - Uji alur membaca modul, pengerjaan kuis, dan penyimpanan hasil skor.
  - Kembali ke `main` dan lakukan squash merge:
    ```bash
    git checkout main
    git merge --squash feature/belajar-kehamilan
    git commit -m "feat: implementasi lengkap fitur belajar kehamilan dan kuis interaktif"
    ```
  - **Catatan**: Branch `feature/belajar-kehamilan` **TIDAK DIHAPUS**.
