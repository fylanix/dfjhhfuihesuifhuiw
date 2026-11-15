# Perencanaan Fitur 2: Catatan Kehamilan (`feature/catatan-kehamilan`)

Fitur ini memungkinkan istri mencatat kondisi fisik, gejala, dan keluhan kehamilan sehari-hari, serta memungkinkan suami melihat riwayat tersebut secara real-time dan memberikan tanggapan/perhatian khusus.

---

## 1. Identifikasi Frame Figma
Berdasarkan section Figma `72:1034` (**Catatan Kehamilan - Naufal Abiyyu Arhab**):
- `72:1666` — **Daftar Catatan Istri - Tampilan Istri**:
  - Header status kehamilan & jumlah catatan.
  - Kartu daftar catatan kronologis (tanggal, kategori keluhan, indikator mood/skala ketidaknyamanan).
  - Badge status respon suami (apakah suami sudah melihat atau membalas).
  - Tombol aksi mengambang / tombol untuk menambah catatan baru.
- `72:1211` — **Tambah Catatan - Tampilan Istri**:
  - Pilihan tanggal & jam.
  - Kategori keluhan (Kaki Bengkak, Mual/Muntah, Kram Perut, Nyeri Punggung, Sakit Kepala, Gerakan Bayi).
  - Skala ketidaknyamanan (1 ringan s/d 5 berat).
  - Textfield input deskripsi yang dirasakan.
  - Tombol konfirmasi "Simpan Catatan".
- `76:2331` — **Catatan Dari Istri - Tampilan Suami**:
  - Kartu catatan istri yang tersinkronisasi langsung.
  - **Tips Suami Pintar**: Panduan praktis medis/empati untuk suami berdasarkan kategori keluhan yang dicatat istri (misal: penanganan kaki bengkak dengan memposisikan kaki lebih tinggi).
  - Form respon cepat untuk suami ("Kirim Tanggapan / Perhatian").
  - Penanda waktu respon suami.

---

## 2. Struktur Komponen & Arsitektur Fitur

### A. Domain Layer
- **Model**: `PregnancyNote`, `SymptomCategory`, `MoodLevel`, `HusbandFeedback`
- **Use Cases**:
  - `GetNotesByCoupleUseCase(coupleId): Flow<List<PregnancyNote>>`
  - `AddNoteUseCase(note): Flow<Resource<Unit>>`
  - `UpdateNoteUseCase(note): Flow<Resource<Unit>>`
  - `DeleteNoteUseCase(noteId): Flow<Resource<Unit>>`
  - `SubmitHusbandResponseUseCase(noteId, responseText): Flow<Resource<Unit>>`
  - `GetHusbandGuidanceTipsUseCase(category): GuidanceTip`

### B. Data Layer
- **Local Room DAO (`NoteDao`)**:
  - `insertNote(note: PregnancyNoteEntity)`
  - `getNotesByCouple(coupleId: String): Flow<List<PregnancyNoteEntity>>`
  - `updateNote(note: PregnancyNoteEntity)`
  - `getPendingSyncNotes(): List<PregnancyNoteEntity>`
- **Firebase Firestore Note Service**:
  - Menyimpan dokumen ke path `/couples/{coupleId}/notes/{noteId}`.
  - Listener real-time `addSnapshotListener` untuk pembaruan instan antar perangkat pasangan.
- **Repository (`NoteRepositoryImpl`)**:
  - Menyimpan ke Room lokal terlebih dahulu untuk kecepatan UI (Offline-First).
  - Mem-push catatan ke Firestore dan menandai `syncStatus = SYNCED`.
  - Menerima update snapshot Firestore dan menyimpannya ke Room lokal.

### C. Presentation Layer (Jetpack Compose)
- `WifeNotesListScreen.kt`: Daftar kartu catatan, filter kategori, badge status, FAB tambah catatan.
- `AddNoteScreen.kt`: Chips selector kategori gejala, rating slider/emoji skala keluhan, form input catatan.
- `HusbandNotesViewScreen.kt`: Kartu catatan dengan aksen visual, kotak tips pendampingan suami, dialog/sheet input tanggapan suami.
- `NotesViewModel.kt`: Mengelola state daftar catatan, status input form, dan event pengiriman tanggapan suami.

---

## 3. Alur Kerja Git Branch
- **Nama Branch**: `feature/catatan-kehamilan`
- **Langkah Pembuatan**:
  ```bash
  git checkout -b feature/catatan-kehamilan
  ```
- **Rangkaian Semantic Commit**:
  1. `feat: buat room entity dan dao untuk catatan kehamilan`
  2. `feat: implementasi firestore data service dan note repository dengan offline-first`
  3. `feat: buat tampilan daftar catatan istri dan kartu gejala dengan jetpack compose`
  4. `feat: buat layar tambah catatan kehamilan beserta validasi input`
  5. `feat: buat tampilan catatan untuk suami disertai modul tips pendampingan dan respon suami`
  6. `feat: hubungkan viewmodel dan flow real-time sync antara istri dan suami`
- **Penyelesaian**:
  - Pengujian penambahan catatan di akun istri dan penerimaan real-time di akun suami.
  - Kembali ke `main` dan lakukan squash merge:
    ```bash
    git checkout main
    git merge --squash feature/catatan-kehamilan
    git commit -m "feat: implementasi lengkap fitur catatan kehamilan dan respon suami"
    ```
  - **Catatan**: Branch `feature/catatan-kehamilan` **TIDAK DIHAPUS**.
