# Daftar Kebutuhan Kredensial & Konfigurasi Firebase

Agar aplikasi **SatuHati** dapat terhubung dan menyinkronkan data secara cloud dan real-time, berikut adalah daftar berkas dan konfigurasi Firebase yang dibutuhkan dari Anda:

---

## 1. Berkas Utama: `google-services.json` (Wajib)

Berkas ini adalah konfigurasi resmi dari Firebase Console untuk aplikasi Android.
- **Lokasi Penempatan**: `app/google-services.json` (di dalam folder [app/](file:///C:/Users/farid/AndroidStudioProjects/SatuHati/app)).
- **Package Name / Android Package ID**: `com.example.satuhati` (harus persis sama dengan yang didaftarkan di Firebase Console).

### Cara Mengunduh `google-services.json`:
1. Buka [Firebase Console](https://console.firebase.google.com/).
2. Buat proyek baru atau pilih proyek yang sudah ada (misal: `satuhati-app`).
3. Tambahkan aplikasi Android dengan Package Name: `com.example.satuhati`.
4. Unduh berkas `google-services.json`.
5. Anda dapat meletakkan berkas tersebut langsung di direktori `app/` atau menempelkan isinya ke chat ini.

---

## 2. Pengaturan Layanan di Firebase Console (Wajib Diaktifkan)

### A. Firebase Authentication
- Buka menu **Build** > **Authentication** > tab **Sign-in method**.
- Aktifkan provider: **Email/Password** (Email dan kata sandi).
- (Opsional: Email link / Anonymous jika ingin login tamu).

### B. Cloud Firestore
- Buka menu **Build** > **Firestore Database**.
- Klik **Create database**.
- Pilih lokasi database yang paling dekat (disarankan: `asia-southeast2` / Jakarta).
- Untuk tahap pengembangan awal, pilih aturan keamanan **Start in test mode** (Mode uji) agar pembacaan & penulisan data antar akun pasangan dapat langsung disinkronkan tanpa kendala hak akses:
  ```javascript
  rules_version = '2';
  service cloud.firestore {
    match /databases/{database}/documents {
      match /{document=**} {
        allow read, write: if request.auth != null; // atau if true saat pengujian
      }
    }
  }
  ```

### C. Cloud Storage (Opsional / Jika ada foto catatan)
- Buka menu **Build** > **Storage**.
- Klik **Get Started** dengan aturan standar pengujian.

---

## 3. Ringkasan Data yang Dapat Anda Berikan

Jika Anda sudah memiliki berkasnya, Anda cukup:
1. Menaruh berkas `google-services.json` ke folder `C:\Users\farid\AndroidStudioProjects\SatuHati\app\google-services.json`, **ATAU**
2. Menyalin (*copy-paste*) isi teks JSON dari berkas `google-services.json` ke dalam chat ini, dan saya yang akan menyimpannya ke direktori yang tepat.
