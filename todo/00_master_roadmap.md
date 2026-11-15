# Master Roadmap & Arsitektur Proyek SatuHati

Dokumen ini merupakan panduan induk implementasi proyek aplikasi mobile **SatuHati** menggunakan **Jetpack Compose**, **Clean Architecture MVVM**, penyimpanan hybrid **Offline-First (Room SQLite + Firebase Firestore/Auth)**, serta alur kolaborasi **Git Multi-Branch** dengan **Semantic Commit** dan **Squash Merge**.

---

## 1. Visi & Tujuan Aplikasi
**SatuHati** adalah aplikasi pendamping kehamilan kolaboratif antara istri dan suami. Aplikasi ini menghubungkan dua akun secara real-time (*couple pairing*) sehingga suami dapat memantau catatan kehamilan istri, mendampingi jadwal pemeriksaan ANC (Antenatal Care), serta belajar bersama seputar kehamilan dan kesehatan ibu-anak.

---

## 2. Tech Stack & Library
- **Bahasa**: Kotlin (JVM 11 / Kotlin 2.x)
- **UI Toolkit**: Jetpack Compose (Material3, Navigation Compose, Compose Lifecycle/ViewModel)
- **Arsitektur**: Clean Architecture (Domain, Data, Presentation) + MVVM
- **State Management**: Kotlin StateFlow, SharedFlow, Compose State
- **Dependency Injection**: Hilt / Manual DI / Koin (direkomendasikan Hilt / Kotlin Coroutines Dispatcher Injection)
- **Local Database**: Room (SQLite Abstraction Layer) + DataStore (Preferences/Session)
- **Cloud Backend**: 
  - Firebase Authentication (Email/Password, Session Token)
  - Firebase Cloud Firestore (Real-time NoSQL sync untuk Akun Pasangan, Catatan, Jadwal, Belajar)
  - Firebase Cloud Storage (Opsional untuk media / attachment foto jika diperlukan)
- **Asynchronous / Reactive**: Kotlin Coroutines & Flow
- **Design System**: Mengikuti spesifikasi Figma SatuHati (Palet warna, tipografi, dan navigasi bottom bar)

---

## 3. Strategi Git Multi-Branch & Semantic Commit

### A. Format Semantic Commit
Setiap commit wajib menggunakan prefix standar Conventional Commits:
- `feat:` Penambahan fitur baru (contoh: `feat: implementasi login dan register form`)
- `fix:` Perbaikan bug (contoh: `fix: perbaiki validasi kode pairing 6 digit`)
- `chore:` Perubahan konfigurasi, gradle, dependency, atau asset (contoh: `chore: konfigurasi firebase dan room dependency`)
- `refactor:` Restrukturisasi kode tanpa mengubah fungsionalitas (contoh: `refactor: pisahkan repository dan remote data source`)
- `docs:` Dokumentasi atau update todo (contoh: `docs: update checklist jadwal kehamilan`)
- `style:` Formatting UI / spacing tanpa mengubah logika

### B. Aturan Multi-Branching
1. **Branch Utama**: `main`
2. **Branch Fitur**:
   - `feature/akun-pasangan`
   - `feature/catatan-kehamilan`
   - `feature/belajar-kehamilan`
   - `feature/jadwal-kehamilan`
3. **Aturan Khusus**:
   - **JANGAN HAPUS** branch fitur setelah pekerjaan selesai (`jangan hapus branch`).
   - Setiap fitur dikembangkan di branchnya masing-masing dengan commit berkala.
   - Setelah branch fitur selesai dan teruji, pindah ke `main` dan lakukan **Squash Merge**:
     ```bash
     git checkout main
     git merge --squash feature/<nama-fitur>
     git commit -m "feat: implementasi lengkap fitur <nama-fitur>"
     ```
   - Branch `feature/<nama-fitur>` tetap dipertahankan sebagai riwayat pengembangan.

---

## 4. Struktur Folder Rencana Kerja (`todo/`)
1. [00_master_roadmap.md](file:///C:/Users/farid/AndroidStudioProjects/SatuHati/todo/00_master_roadmap.md) — Master roadmap & standard operasional.
2. [01_architecture_and_data_layer.md](file:///C:/Users/farid/AndroidStudioProjects/SatuHati/todo/01_architecture_and_data_layer.md) — Desain Clean Architecture, Room Database, Firebase Schema, dan Sync Engine.
3. [02_feature_akun_pasangan.md](file:///C:/Users/farid/AndroidStudioProjects/SatuHati/todo/02_feature_akun_pasangan.md) — Rencana fitur Landing, Register, Login, Generate/Enter Pairing Code.
4. [03_feature_catatan_kehamilan.md](file:///C:/Users/farid/AndroidStudioProjects/SatuHati/todo/03_feature_catatan_kehamilan.md) — Rencana fitur Catatan Istri, Tambah Catatan, dan Tampilan Suami.
5. [04_feature_belajar_kehamilan.md](file:///C:/Users/farid/AndroidStudioProjects/SatuHati/todo/04_feature_belajar_kehamilan.md) — Rencana fitur Katalog Belajar, Modul Bacaan, Kuis Interaktif, dan Evaluasi.
6. [05_feature_jadwal_kehamilan.md](file:///C:/Users/farid/AndroidStudioProjects/SatuHati/todo/05_feature_jadwal_kehamilan.md) — Rencana fitur Jadwal ANC K1-K6, Form Jadwal, dan Panduan Suami.
7. [06_git_workflow_and_task_checklist.md](file:///C:/Users/farid/AndroidStudioProjects/SatuHati/todo/06_git_workflow_and_task_checklist.md) — Checklist bertahap pengerjaan per branch dan perintah git.
8. [07_ui_design_specs_from_figma.md](file:///C:/Users/farid/AndroidStudioProjects/SatuHati/todo/07_ui_design_specs_from_figma.md) — Spesifikasi token desain UI, palet warna, tipografi Manrope, dan layout presisi dari Figma.
9. [08_firebase_credentials_requirement.md](file:///C:/Users/farid/AndroidStudioProjects/SatuHati/todo/08_firebase_credentials_requirement.md) — Daftar berkas (`google-services.json`) dan konfigurasi Firebase yang dibutuhkan.
