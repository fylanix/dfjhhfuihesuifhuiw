# Spesifikasi Desain UI SatuHati Berdasarkan Figma

Dokumen ini merangkum token desain, warna, tipografi, komponen global, serta layout spesifik setiap layar berdasarkan ekstraksi langsung dari file Figma **SatuHati** (`Tci1DMz9C2rd6bruWPbUFl`).

---

## 1. Design Tokens & Color Palette

Berdasarkan inspeksi langsung komponen Figma:

### A. Palet Warna Utama (Primary & Accents)
- **Primary Brand Blue**: `#3952E0` (Digunakan untuk CTA button, tab terpilih, link aktif, header aksen)
- **Primary Dark Navy (Banner Header)**: `#1E2E86` (Header latar belakang lengkung bergradasi pada modul Diari dan Belajar)
- **Dark Primary Header**: `#1735C8` (Tombol aksi sekunder header)
- **Secondary Light Blue (Soft Tint)**: `#E7EBFE` (Latar belakang ikon header, badge)
- **Text Headings / Dark Neutral**: `#14213D` (Warna judul teks, label penting, bobot tebal)
- **Text Body / Muted Neutral**: `#64748B` & `#6B7490` (Warna deskripsi teks, subjudul, ikon tidak aktif)
- **Border / Divider Neutral**: `#E2E8F0` (Border input field, border container)
- **Input Background Light**: `#F8FAFC` (Field input text)
- **Surface / Background**: `#FFFFFF` & `#FAF8FF`

### B. Palet Status & Indikator Medis
- **Success / Terbaca / Sehat**:
  - Hijau Utama: `#3FA373` & `#287D57`
  - Hijau Lembut (Background Badge): `#EDF7F1` atau `rgba(63, 163, 115, 0.15)`
- **Warning / Trimester Milestone**:
  - Oranye / Amber: `#F2A93C`
  - Oranye Lembut (Background Badge): `rgba(242, 169, 60, 0.2)`
- **Trimester Capsule Accent**:
  - Capsule Latar: `rgba(255, 255, 255, 0.15)`
  - Capsule Teks: `#DEE0FF`

### C. Tipografi
- **Font Family**: **Manrope** (Google Fonts: Bold, SemiBold, Medium, Regular)
- **Ukuran & Hierarki**:
  - `Display / Heading 1`: 28sp - 36sp (Bold, line-height 34sp - 46sp)
  - `Heading 2`: 22sp (Bold, letter-spacing -0.55sp)
  - `Heading 3 / Subtitle`: 18sp (Bold / SemiBold)
  - `Body Normal`: 14sp - 15sp (Regular, line-height 20sp - 22sp)
  - `Caption / Micro Label`: 12sp - 13sp (Medium / SemiBold, letter-spacing 0.12sp - 0.24sp)

### D. Shape & Radii
- **Buttons / Textfields**: `12dp` radius
- **Cards / Containers**: `16dp` - `20dp` - `24dp` radius
- **Capsules / Badges / Bottom Nav Pill**: `9999dp` (Fully Rounded / Circle)
- **Banner Bawah Header**: `rounded-bl-[32dp]` & `rounded-br-[32dp]`

---

## 2. Navigasi Global: Bottom Navigation Bar (`11:980`)

Sesuai frame Figma `11:980` (**Nav**):
- **Tinggi**: 64dp
- **Latar Belakang**: `rgba(255, 255, 255, 0.95)` dengan efek blur (backdrop blur 12dp) dan shadow halus `0px -4px 16px rgba(20,33,61,0.04)`.
- **5 Menu Destinasi**:
  1. **Beranda** (Ikon Rumah)
  2. **Diari** (Ikon Buku/Catatan Gejala Istri)
  3. **Belajar** (Ikon Toga/Modul Edukasi)
  4. **Jadwal** (Ikon Kalender ANC)
  5. **Akun** (Ikon Profil Pengguna)
- **Status Aktif**: Teks & Ikon berwarna `#3952E0` (Primary Blue).
- **Status Tidak Aktif**: Teks & Ikon berwarna `#6B7490` (Muted Neutral).

---

## 3. Detail Layar per Fitur Berdasarkan Figma

### A. Fitur Akun Pasangan (`feature/akun-pasangan`)
1. **Landing Screen (`55:63`)**:
   - Logo teks: "SATUHATI" `#3952E0` (Bold 18sp).
   - Ilustrasi utama (`330dp x 330dp`).
   - Teks Selamat Datang: "Selamat datang di SATUHATI" (28sp Bold).
   - Subjudul: "Masuk ke akunmu atau daftar akun baru untuk memulai."
   - Dua tombol aksi vertikal 56dp:
     - "Masuk" (Solid Blue `#3952E0`, teks putih).
     - "Daftar" (Outlined Border `#E2E8F0`, teks `#3952E0`).
2. **Login Screen (`48:1247`)**:
   - Header aplikasi dengan badge ikon SATUHATI + label "Masuk ke akun" (28sp Bold).
   - Deskripsi instruksi form.
   - Textfield Email & Password dengan background `#F8FAFC`, border `#E2E8F0`, rounded 12dp.
   - Checkbox persetujuan Syarat & Ketentuan serta Kebijakan Privasi SATUHATI.
   - Tombol solid "Masuk" (56dp height).
   - Footer link ke layar Registrasi ("Belum memiliki akun? Daftar").
3. **Register Screen (`48:1244`)**:
   - Form pembuatan akun baru dengan kolom identitas, peran (Istri/Suami), email, dan password.
4. **Link - Show Code (`53:1312`) & Enter Code (`84:144`)**:
   - Layar pembuatan & input 6 digit kode pasangan untuk sinkronisasi akun.

### B. Fitur Catatan Kehamilan / Diari (`feature/catatan-kehamilan`)
1. **Daftar Catatan Istri (`72:1666`)**:
   - Top Header Banner lengkung biru tua (`#1E2E86`) dengan capsule "Pasangan Trimester 2" (`#DEE0FF`) dan "Minggu ke-24" (`#F2A93C`).
   - Tombol profil/peran "Bunda".
   - Status sinkronisasi: "✅ Semua Terbaca Suami" (`#3FA373`).
   - List kartu catatan ber-radius 20dp:
     - Ikon emosi/mood (Senang, Netral, dll.).
     - Tanggal & jam ("15 Oktober 2026 - 21.25").
     - Teks keluhan/catatan harian.
     - Badge status "✅ Terbaca".
   - Tombol mengambang / pill di atas bottom nav: "Tambah catatan" (`#3952E0` rounded 9999dp).
2. **Tambah Catatan (`72:1211`)**:
   - Form modal untuk memilih kategori gejala, skala rasa sakit/mood, dan deskripsi.
3. **Tampilan Catatan untuk Suami (`76:2331`)**:
   - Tampilan real-time catatan istri disertai box "Tips Suami Pintar" dan kolom kirim tanggapan.

### C. Fitur Belajar Kehamilan (`feature/belajar-kehamilan`)
1. **Katalog Belajar (`8:925` / `100:1986`)**:
   - Tab seleksi Trimester 1, Trimester 2, Trimester 3, dan Pasca Salin.
   - Kartu artikel dengan estimasi waktu baca dan tag status pemahaman.
2. **Modul Bacaan (`13:1007` / `100:2291`)**:
   - Artikel komprehensif, poin penting klinis, dan tombol "Uji Pemahaman".
3. **Kuis Interaktif Kaki Bengkak (`8:922` / `100:2514`)**:
   - Bar progres 66% bersegmen.
   - Role partnership badge.
   - Kartu pertanyaan skenario dan pilihan ganda (A, B, C, D) dengan efek highlight status benar/salah.
   - **Instant Clinical Feedback Accordion**: Accordion drop-down penjelasan patofisiologi medis di bawah opsi yang dipilih.
4. **Hasil Evaluasi Kuis (`8:923` / `100:2690`)**:
   - Skor pemahaman, badge pencapaian, dan rekomendasi konsultasi ANC.

### D. Fitur Jadwal Kehamilan / ANC (`61:4` & `100:3863`)
1. **Jadwal Pemeriksaan ANC (`61:4`)**:
   - Top Bar SATUHATI + Jadwal + Ikon Notifikasi edukasi.
   - Kalender bulanan & penanda hari ini ("Hari ini · 15 Oktober", `#287D57`).
   - Judul bulan besar: "Oktober 2026" (36sp Bold).
   - Timeline ANC K1-K6 dengan faskes, dokter, dan status pemeriksaan.
2. **Form Tambah Jadwal (`79:757`)**:
   - Input tanggal, jam, faskes, dokter, serta catatan pengingat.
3. **Jadwal Suami (`96:2171`)**:
   - Checklist persiapan suami (Buku KIA, Kartu BPJS, transportasi, daftar pertanyaan).
