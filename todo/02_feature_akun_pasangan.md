# Perencanaan Fitur 1: Akun Pasangan (`feature/akun-pasangan`)

Fitur ini mencakup alur registrasi, autentikasi, pemilihan peran (Istri/Suami), serta mekanisme penghubungan akun pasangan (*Couple Pairing*) berbasis kode 6 karakter.

---

## 1. Identifikasi Frame Figma
Berdasarkan section Figma `45:2` (**Akun Pasangan - Fazle Adyuta Utomo**):
- `55:63` — **Landing Screen**: Pemilihan peran (Istri vs Suami), tagline SatuHati, tombol mulai.
- `48:1244` — **Register Screen**: Form pembuatan akun (Nama, Email, No HP, Password, Peran).
- `48:1247` — **Login Screen**: Form masuk dengan validasi kredensial.
- `53:1312` — **Link - Show Code Screen**: Layar bagi pembuat kode (biasanya Istri atau pengguna pertama) yang menampilkan 6 digit kode pairing, tombol bagikan (WhatsApp/Salin), dan deteksi otomatis saat pasangan terhubung.
- `84:144` — **Link - Enter Code Screen**: Layar input 6 digit kode unik bagi pasangan (Suami/pengguna kedua) untuk menautkan akun ke sesi kehamilan yang sama.

---

## 2. Struktur Komponen & Arsitektur Fitur

### A. Domain Layer
- **Model**: `User`, `Couple`, `Role` (Enum: `WIFE`, `HUSBAND`), `PairingStatus` (Enum: `PENDING`, `CONNECTED`)
- **Use Cases**:
  - `LoginUseCase(email, password): Flow<Resource<User>>`
  - `RegisterUseCase(name, email, password, phone, role): Flow<Resource<User>>`
  - `GeneratePairingCodeUseCase(userId): Flow<Resource<String>>`
  - `ConnectPairingCodeUseCase(userId, code): Flow<Resource<Couple>>`
  - `ObservePairingStatusUseCase(coupleId): Flow<Boolean>`
  - `GetActiveSessionUseCase(): Flow<User?>`
  - `LogoutUseCase(): Flow<Unit>`

### B. Data Layer
- **Firebase Auth Service**: Mengelola `signInWithEmailAndPassword`, `createUserWithEmailAndPassword`, dan `signOut`.
- **Firestore User & Couple Service**:
  - Membuat dokumen `users/{uid}`
  - Membuat/memvalidasi dokumen `couples/{coupleId}` dengan field `pairingCode` yang unik.
  - Query dokumen `couples` berdasarkan `pairingCode`.
- **Room Database (Local)**:
  - Menyimpan profil pengguna aktif ke `users` tabel.
  - Menyimpan cache status `couple` agar pengguna tidak perlu login/cek ulang saat membuka aplikasi secara offline.

### C. Presentation Layer (Jetpack Compose)
- `LandingScreen.kt`: Kartu pemilihan peran interaktif, branding visual.
- `LoginScreen.kt`: Input email, password, error message state, link ke pendaftaran.
- `RegisterScreen.kt`: Input lengkap, toggle visibilitas password, validasi format.
- `ShowPairingCodeScreen.kt`: Tampilan besar kode 6 digit, tombol *Copy to Clipboard*, tombol *Share to WhatsApp*, indikator pulsa / listening perubahan status.
- `EnterPairingCodeScreen.kt`: Kotak input 6 digit berfokus otomatis, tombol verifikasi kode.
- `AuthViewModel.kt`: Menyimpan `AuthState`, `PairingState`, dan menangani *one-time event* (navigasi ke Dashboard).

---

## 3. Alur Kerja Git Branch
- **Nama Branch**: `feature/akun-pasangan`
- **Langkah Pembuatan**:
  ```bash
  git checkout -b feature/akun-pasangan
  ```
- **Rangkaian Semantic Commit**:
  1. `chore: setup firebase auth and firestore dependencies`
  2. `feat: tambahkan domain model dan repository interface akun pasangan`
  3. `feat: implementasi local room dao dan remote firebase auth service`
  4. `feat: implementasi ui landing, login, dan register dengan jetpack compose`
  5. `feat: buat sistem generate dan input kode pairing pasangan 6 digit`
  6. `feat: integrasi auth viewmodel dan navigasi sesi akun pasangan`
- **Penyelesaian**:
  - Lakukan pengujian form dan pairing.
  - Kembali ke `main` dan lakukan squash merge:
    ```bash
    git checkout main
    git merge --squash feature/akun-pasangan
    git commit -m "feat: implementasi lengkap fitur akun pasangan dan pairing kode"
    ```
  - **Catatan**: Branch `feature/akun-pasangan` **TIDAK DIHAPUS**.
