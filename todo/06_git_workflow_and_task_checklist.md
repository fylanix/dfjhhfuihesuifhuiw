# Git Workflow & Checklist Eksekusi Proyek

Dokumen ini adalah lembar kerja dan checklist bertahap yang harus diikuti selama pengerjaan proyek SatuHati.

---

## 1. Aturan Emas Git & Branching

1. **Format Commit Semantic**:
   - `feat: <deskripsi fitur baru>`
   - `fix: <deskripsi perbaikan>`
   - `chore: <deskripsi dependency / config / setup>`
   - `refactor: <deskripsi restrukturisasi kode>`
   - `docs: <deskripsi dokumentasi>`
2. **Multi-Branching**:
   - Fitur dikerjakan pada branch terpisah:
     1. `feature/akun-pasangan`
     2. `feature/catatan-kehamilan`
     3. `feature/belajar-kehamilan`
     4. `feature/jadwal-kehamilan`
3. **Penggabungan ke Main**:
   - Gunakan `git merge --squash feature/<nama-branch>`.
   - **DILARANG MENGHAPUS BRANCH FITUR** setelah pengerjaan selesai (`git branch -d` tidak dijalankan).

---

## 2. Checklist Tahapan Pengerjaan

### Fase 0: Inisialisasi Git & Pondasi Dasar Proyek (`main`)
- [ ] Inisialisasi git repository (`git init`) dan pastikan `.gitignore` mencakup build/cache Android.
- [ ] Tambahkan konfigurasi dependencies ke `gradle/libs.versions.toml`:
  - Firebase BoM, Firebase Auth, Firebase Firestore
  - Room Runtime, Room KTX, Room Compiler (KSP)
  - Navigation Compose & ViewModel Compose
  - Material Icons Extended
- [ ] Konfigurasi `build.gradle.kts` (Project & App level) + plugin KSP & Google Services.
- [ ] Setup tema dasar (Colors, Typography, Theme Material3) sesuai desain Figma SatuHati.
- [ ] Commit awal di `main`:
  - `chore: inisialisasi git repository dan struktur project satuhati`
  - `chore: konfigurasi dependencies room database, firebase, dan navigation compose`
  - `feat: setup theming dasar material 3 dan scaffold navigasi aplikasi`

---

### Fase 1: Fitur Akun Pasangan (`feature/akun-pasangan`)
- [ ] Buat dan pindah ke branch: `git checkout -b feature/akun-pasangan`
- [ ] Buat model domain `User`, `Couple`, dan repository interface `IAuthRepository`.
- [ ] Buat implementasi lokal `UserEntity`, `UserDao`, dan `SatuHatiRoomDatabase`.
- [ ] Buat `FirebaseAuthService` dan `FirestoreCoupleService`.
- [ ] Implementasikan `AuthRepositoryImpl` dengan offline-first caching.
- [ ] Buat UI Jetpack Compose:
  - [ ] `LandingScreen` (Pilihan peran Istri & Suami).
  - [ ] `RegisterScreen` (Form pendaftaran).
  - [ ] `LoginScreen` (Form autentikasi).
  - [ ] `ShowPairingCodeScreen` (Tampilan kode 6 digit & share).
  - [ ] `EnterPairingCodeScreen` (Input 6 digit & verifikasi pasangan).
- [ ] Buat `AuthViewModel` dan hubungkan state navigasi.
- [ ] Commit berkala dengan semantic commit:
  - `feat: buat domain model dan kontrak repository akun pasangan`
  - `feat: implementasi local room dao dan remote firebase auth service`
  - `feat: buat tampilan landing, login, dan register akun pasangan`
  - `feat: implementasi alur generate dan input kode pairing 6 digit`
  - `feat: integrasi auth viewmodel dan sesi pengguna`
- [ ] Merge ke `main` dengan squash:
  ```bash
  git checkout main
  git merge --squash feature/akun-pasangan
  git commit -m "feat: implementasi lengkap fitur akun pasangan dan alur pairing kode"
  ```
- [ ] **Pastikan branch `feature/akun-pasangan` tetap ada**.

---

### Fase 2: Fitur Catatan Kehamilan (`feature/catatan-kehamilan`)
- [ ] Buat dan pindah ke branch dari `main`: `git checkout -b feature/catatan-kehamilan`
- [ ] Buat model domain `PregnancyNote` dan `INoteRepository`.
- [ ] Buat `PregnancyNoteEntity`, `NoteDao`, dan migrasi Room Database.
- [ ] Buat `FirestoreNoteService` dengan listener real-time sync.
- [ ] Buat `NoteRepositoryImpl` (Offline-first Room + Firestore push/pull).
- [ ] Buat UI Jetpack Compose:
  - [ ] `WifeNotesListScreen` (Daftar catatan, status respon suami, FAB).
  - [ ] `AddNoteScreen` (Pilihan keluhan, skala keparahan, deskripsi).
  - [ ] `HusbandNotesViewScreen` (Daftar keluhan istri, tips medis suami, respon cepat).
- [ ] Buat `NotesViewModel` untuk pengelolaan state catatan & sinkronisasi.
- [ ] Commit berkala dengan semantic commit:
  - `feat: buat entitas room dan dao untuk catatan kehamilan`
  - `feat: implementasi firestore service dan note repository offline-first`
  - `feat: buat layar daftar catatan dan form tambah catatan istri`
  - `feat: buat layar catatan untuk suami beserta tips pendampingan dan respon`
  - `feat: hubungkan viewmodel dan flow sinkronisasi real-time catatan`
- [ ] Merge ke `main` dengan squash:
  ```bash
  git checkout main
  git merge --squash feature/catatan-kehamilan
  git commit -m "feat: implementasi lengkap fitur catatan kehamilan dan respon suami"
  ```
- [ ] **Pastikan branch `feature/catatan-kehamilan` tetap ada**.

---

### Fase 3: Fitur Belajar Kehamilan (`feature/belajar-kehamilan`)
- [ ] Buat dan pindah ke branch dari `main`: `git checkout -b feature/belajar-kehamilan`
- [ ] Buat model domain `LearningModule`, `QuizQuestion`, `QuizResult`.
- [ ] Buat entitas `LearningModuleEntity`, `QuizResultEntity`, dan `LearningDao`.
- [ ] Buat seed/dummy dataset konten edukasi kehamilan terverifikasi klinis per trimester.
- [ ] Buat UI Jetpack Compose:
  - [ ] `LearningCatalogScreen` (Tab trimester 1-3 & nifas, card modul).
  - [ ] `ModuleDetailScreen` (Bacaan artikel, poin kunci, tips suami).
  - [ ] `QuizScreen` (Progress bar, pertanyaan skenario, opsi A-D, accordion feedback klinis).
  - [ ] `QuizResultScreen` (Skor kelulusan, badge pencapaian, rekomendasi).
- [ ] Buat `LearningViewModel` untuk navigasi soal, feedback klinis, dan skor.
- [ ] Commit berkala dengan semantic commit:
  - `feat: buat model domain dan dataset edukasi kehamilan terstruktur`
  - `feat: buat katalog modul pembelajaran berbasis trimester`
  - `feat: buat modul bacaan lengkap dan tips pendampingan kehamilan`
  - `feat: buat kuis interaktif dengan umpan balik klinis instan`
  - `feat: buat tampilan evaluasi hasil kuis dan skor pemahaman`
- [ ] Merge ke `main` dengan squash:
  ```bash
  git checkout main
  git merge --squash feature/belajar-kehamilan
  git commit -m "feat: implementasi lengkap fitur belajar kehamilan dan kuis interaktif"
  ```
- [ ] **Pastikan branch `feature/belajar-kehamilan` tetap ada**.

---

### Fase 4: Fitur Jadwal Kehamilan (`feature/jadwal-kehamilan`)
- [ ] Buat dan pindah ke branch dari `main`: `git checkout -b feature/jadwal-kehamilan`
- [ ] Buat model domain `AncSchedule`, `ScheduleStatus`, `HusbandPreparationTask`.
- [ ] Buat `AncScheduleEntity`, `ScheduleDao`, dan `FirestoreScheduleService`.
- [ ] Buat `ScheduleRepositoryImpl` dengan sinkronisasi dua arah.
- [ ] Buat UI Jetpack Compose:
  - [ ] `AncTimelineScreen` (Timeline kunjungan K1-K6 Kemenkes, countdown).
  - [ ] `AddScheduleScreen` (Form tambah jadwal, date/time picker, faskes, dokter).
  - [ ] `HusbandScheduleScreen` (Checklist persiapan kontrol suami: buku KIA, BPJS, transport).
- [ ] Buat `ScheduleViewModel` untuk logika status kunjungan dan checklist.
- [ ] Commit berkala dengan semantic commit:
  - `feat: buat entitas room dan dao untuk jadwal pemeriksaan anc`
  - `feat: implementasi service firestore dan repositori jadwal kehamilan`
  - `feat: buat layar timeline pemeriksaan anc k1 sampai k6 standar kemenkes`
  - `feat: buat form tambah dan atur jadwal konsultasi dokter atau bidan`
  - `feat: buat tampilan jadwal suami beserta checklist persiapan kontrol`
- [ ] Merge ke `main` dengan squash:
  ```bash
  git checkout main
  git merge --squash feature/jadwal-kehamilan
  git commit -m "feat: implementasi lengkap fitur jadwal anc kehamilan dan checklist suami"
  ```
- [ ] **Pastikan branch `feature/jadwal-kehamilan` tetap ada**.

---

### Fase 5: Integrasi Navigasi & Bottom Navigation Bar (`main`)
- [ ] Implementasikan `BottomNavigationBar` (Navigasi: Belajar, Catatan, Jadwal, Akun) sesuai desain Figma (`11:980`).
- [ ] Integrasikan `NavHost` utama yang menghubungkan seluruh fitur.
- [ ] Pengujian menyeluruh (*End-to-End*) alur Istri dan Suami.
- [ ] Commit akhir:
  - `feat: integrasikan bottom navigation bar dan navhost satuhati`
  - `chore: finalisasi sinkronisasi dan pengujian aplikasi`
