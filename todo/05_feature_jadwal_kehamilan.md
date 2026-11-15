# Perencanaan Fitur 4: Jadwal Kehamilan (`feature/jadwal-kehamilan`)

Fitur ini mengatur jadwal pemeriksaan kehamilan berkala (ANC - Antenatal Care) sesuai standar Kemenkes RI (minimal 6 kali kunjungan K1-K6), formulir penambahan jadwal kontrol, serta sinkronisasi checklist pendampingan untuk suami.

---

## 1. Identifikasi Frame Figma
Berdasarkan section Figma `100:3863` (**Jadwal Kehamilan - Rananda Ardiawan**):
- `61:4` — **Jadwal Pemeriksaan ANC**:
  - Ringkasan kunjungan ANC terdekat & hitung mundur hari (*countdown*).
  - Garis waktu kronologis (Timeline K1 - K6) dengan status (Selesai, Mendatang, Lewat).
  - Kartu informasi: Fasilitas kesehatan (Puskesmas/RS/Klinik), Dokter/Bidan, tanggal & jam.
  - Tombol untuk menambah atau menjadwalkan pemeriksaan baru.
- `79:757` — **Form Tambah Jadwal**:
  - Pemilihan tipe pemeriksaan (ANC Standar Kemenkes K1-K6, USG Fetomaternal, Skrining Lab, Imunisasi TT).
  - Tanggal & waktu periksa (DatePicker & TimePicker).
  - Input Nama Fasilitas Kesehatan dan Tenaga Medis (Dokter/Bidan).
  - Kolom catatan keluhan/pertanyaan yang ingin diajukan ke dokter.
  - Tombol simpan jadwal.
- `96:2171` — **Jadwal pemeriksaan — Suami**:
  - Tampilan sinkron di perangkat suami.
  - **Checklist Persiapan Suami**:
    - Buku KIA (Kesehatan Ibu & Anak)
    - Kartu BPJS / Asuransi & Surat Rujukan
    - KTP Suami & Istri
    - Perencanaan transportasi & estimasi waktu tempuh
    - Daftar pertanyaan penting untuk dokter
  - Status kesiapan suami yang dapat dicentang langsung.

---

## 2. Struktur Komponen & Arsitektur Fitur

### A. Domain Layer
- **Model**: `AncSchedule`, `VisitType`, `HusbandPreparationTask`, `ScheduleStatus`
- **Use Cases**:
  - `GetSchedulesByCoupleUseCase(coupleId): Flow<List<AncSchedule>>`
  - `GetNextUpcomingScheduleUseCase(coupleId): Flow<AncSchedule?>`
  - `CreateOrUpdateScheduleUseCase(schedule): Flow<Resource<Unit>>`
  - `ToggleScheduleCompletionUseCase(scheduleId, isCompleted): Flow<Unit>`
  - `ToggleHusbandChecklistTaskUseCase(scheduleId, taskId, isChecked): Flow<Unit>`

### B. Data Layer
- **Local Room Database (`ScheduleDao`)**:
  - CRUD entitas `AncScheduleEntity`.
  - Simpan checklist persiapan suami sebagai struktur JSON di SQLite.
  - Kemampuan offline penuh untuk melihat jadwal tanpa internet.
- **Firebase Firestore Schedule Service**:
  - Jalur sinkronisasi: `/couples/{coupleId}/schedules/{scheduleId}`.
  - Setiap perubahan jadwal oleh istri langsung terbarui di aplikasi suami via listener real-time.

### C. Presentation Layer (Jetpack Compose)
- `AncTimelineScreen.kt`: Kartu countdown hero, visual vertical timeline K1-K6, status badge chip.
- `AddScheduleScreen.kt`: Dropdown tipe kunjungan, date/time picker dialog, textfield faskes & dokter, tombol simpan.
- `HusbandScheduleScreen.kt`: Kartu ringkasan kontrol, daftar interaktif checkbox persiapan pendampingan suami, tips konsultasi dokter.
- `ScheduleViewModel.kt`: Mengelola flow jadwal, validasi form tanggal, dan update toggle checklist suami.

---

## 3. Alur Kerja Git Branch
- **Nama Branch**: `feature/jadwal-kehamilan`
- **Langkah Pembuatan**:
  ```bash
  git checkout -b feature/jadwal-kehamilan
  ```
- **Rangkaian Semantic Commit**:
  1. `feat: buat entitas room dan dao untuk jadwal anc kehamilan`
  2. `feat: implementasi remote firestore service dan repository jadwal`
  3. `feat: buat tampilan timeline jadwal anc k1 sampai k6 standar kemenkes`
  4. `feat: buat formulir tambah dan edit jadwal pemeriksaan beserta date picker`
  5. `feat: buat tampilan jadwal suami dengan checklist interaktif persiapan kontrol`
  6. `feat: integrasi jadwal viewmodel dan sinkronisasi real-time antar pasangan`
- **Penyelesaian**:
  - Uji penambahan jadwal dan pembaruan checklist persiapan di kedua akun.
  - Kembali ke `main` dan lakukan squash merge:
    ```bash
    git checkout main
    git merge --squash feature/jadwal-kehamilan
    git commit -m "feat: implementasi lengkap fitur jadwal anc kehamilan dan checklist suami"
    ```
  - **Catatan**: Branch `feature/jadwal-kehamilan` **TIDAK DIHAPUS**.
