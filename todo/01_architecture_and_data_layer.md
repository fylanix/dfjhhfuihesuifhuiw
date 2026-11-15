# Arsitektur & Desain Data Layer (Clean Architecture & MVVM)

Dokumen ini merinci struktur Clean Architecture, arsitektur data lokal (Room Database/SQLite) dan data cloud (Firebase Firestore & Auth), serta pola Service dan Repository.

---

## 1. Diagram Alur Clean Architecture

```
+-------------------------------------------------------------------+
|                        PRESENTATION LAYER                         |
|  - Jetpack Compose Screens (UI)                                   |
|  - Custom Components (Card, Badge, Button, Input)                 |
|  - ViewModel (StateFlow, UiState, UiEvent)                        |
+---------------------------------+---------------------------------+
                                  | Mengamati State / Panggil UseCase
                                  v
+-------------------------------------------------------------------+
|                          DOMAIN LAYER                             |
|  - Domain Entities / Models (Pure Kotlin Data Classes)            |
|  - Use Cases / Interactors (Logika Bisnis Independen)             |
|  - Repository Interfaces (Kontrak Abstraksi Data)                 |
+---------------------------------+---------------------------------+
                                  | Implementasi Kontrak
                                  v
+-------------------------------------------------------------------+
|                           DATA LAYER                              |
|  - Repository Implementations (Offline-First Single Source Truth) |
|  - Local Data Source (Room Database, DAOs, SQLite Entities)       |
|  - Remote Data Source (Firebase Auth, Firestore Services, DTOs)   |
|  - Mappers (Entity <-> Domain Model <-> DTO)                      |
+-------------------------------------------------------------------+
```

---

## 2. Struktur Paket Rekomendasi (`com.example.satuhati`)

```
com.example.satuhati/
│
├── core/
│   ├── common/              # Resource, Result, UiText, DateUtils
│   ├── database/            # SatuHatiRoomDatabase, Converters
│   ├── network/             # FirebaseClients, NetworkMonitor
│   └── di/                  # ServiceLocator / Hilt Dependency Modules
│
├── data/
│   ├── local/
│   │   ├── dao/             # UserDao, CoupleDao, NoteDao, LearningDao, ScheduleDao
│   │   └── entity/          # UserEntity, NoteEntity, ScheduleEntity, ModuleEntity
│   ├── remote/
│   │   ├── service/         # FirebaseAuthService, FirestoreService
│   │   └── dto/             # UserDto, NoteDto, ScheduleDto, QuizDto
│   ├── mapper/              # Mapper extension functions
│   └── repository/          # AuthRepoImpl, NoteRepoImpl, LearningRepoImpl, ScheduleRepoImpl
│
├── domain/
│   ├── model/               # User, Couple, PregnancyNote, LearningModule, AncSchedule
│   ├── repository/          # IAuthRepository, INoteRepository, ILearningRepository, IScheduleRepository
│   └── usecase/             # GetNotesUseCase, PairCoupleUseCase, SaveScheduleUseCase, dll.
│
├── presentation/
│   ├── common/              # BottomNavBar, SatuHatiTopBar, Buttons, Modals
│   ├── navigation/          # Screen Routes, NavGraph, BottomNavDestination
│   ├── auth/                # LandingScreen, LoginScreen, RegisterScreen, PairingScreen, AuthViewModel
│   ├── notes/               # WifeNotesScreen, AddNoteScreen, HusbandNotesScreen, NotesViewModel
│   ├── learning/            # CatalogScreen, ModuleDetailScreen, QuizScreen, QuizResultScreen, LearningViewModel
│   ├── schedule/            # ScheduleListScreen, AddScheduleScreen, HusbandScheduleScreen, ScheduleViewModel
│   └── theme/               # Color, Type, Shape, Theme (Material3)
│
└── SatuHatiApp.kt           # Application Class
```

---

## 3. Desain Penyimpanan Data Lokal (Room Database / SQLite)

### Nama Database: `satuhati_database.db`

### Tabel & Entitas:

#### A. Tabel `users` (`UserEntity`)
- `id` (String, PK) — Firebase UID
- `email` (String)
- `name` (String)
- `phoneNumber` (String)
- `role` (String) — `"ISTRI"` atau `"SUAMI"`
- `coupleId` (String?, Nullable) — ID relasi pasangan
- `partnerName` (String?, Nullable)
- `pairingCode` (String?, Nullable) — Kode unik 6 digit
- `updatedAt` (Long)

#### B. Tabel `pregnancy_notes` (`PregnancyNoteEntity`)
- `id` (String, PK) — UUID / Firestore Document ID
- `coupleId` (String, Index)
- `authorId` (String) — ID Istri
- `dateMillis` (Long)
- `category` (String) — Misal: "Kaki Bengkak", "Mual & Muntah", "Sakit Pinggang", "Gerakan Janin"
- `mood` (String) — "Baik", "Cemas", "Lelah", "Sakit"
- `severityLevel` (Int) — Skala 1 - 5
- `content` (String) — Keluhan atau deskripsi detail
- `husbandResponse` (String?, Nullable) — Catatan/tanggapan dari suami
- `husbandRespondedAt` (Long?, Nullable)
- `syncStatus` (Int) — 0: Synced, 1: Pending Sync, 2: Error
- `createdAt` (Long)
- `updatedAt` (Long)

#### C. Tabel `anc_schedules` (`AncScheduleEntity`)
- `id` (String, PK)
- `coupleId` (String, Index)
- `visitNumber` (Int) — 1 s/d 6 (Sesuai ANC K1 - K6 Kemenkes)
- `title` (String) — Misal: "ANC Trimester 1 (K1) - Dokter Spesialis"
- `scheduledDateTime` (Long) — Tanggal dan waktu periksa
- `faskesName` (String) — Puskesmas / RS / Klinik
- `doctorOrMidwifeName` (String)
- `isCompleted` (Boolean) — Status selesai
- `notes` (String?) — Catatan keluhan / pengingat
- `husbandPreparationChecklistJson` (String) — JSON checklist persiapan suami (buku KIA, BPJS, transport)
- `syncStatus` (Int)
- `updatedAt` (Long)

#### D. Tabel `learning_modules` (`LearningModuleEntity`)
- `id` (String, PK)
- `trimester` (Int) — 1, 2, 3, atau 4 (Pasca Salin)
- `title` (String)
- `summary` (String)
- `estimatedReadingTimeMinutes` (Int)
- `contentBody` (String) — Markdown / formatted text
- `isRead` (Boolean)
- `quizId` (String?)

#### E. Tabel `quiz_results` (`QuizResultEntity`)
- `id` (String, PK)
- `moduleId` (String)
- `userId` (String)
- `score` (Int) — 0 - 100
- `totalQuestions` (Int)
- `correctAnswers` (Int)
- `status` (String) — "LULUS" / "PERLU REVIEW"
- `evaluatedAt` (Long)

---

## 4. Desain Penyimpanan Cloud (Firebase Firestore Schema)

### Struktur Koleksi Firestore:

```
/users/{userId}
  ├── id: String
  ├── email: String
  ├── name: String
  ├── phoneNumber: String
  ├── role: "ISTRI" | "SUAMI"
  ├── coupleId: String?
  ├── pairingCode: String?
  └── createdAt: Timestamp

/couples/{coupleId}
  ├── id: String
  ├── wifeId: String
  ├── husbandId: String?
  ├── pairingCode: String (Unique 6 digits alphanumeric)
  ├── isConnected: Boolean
  ├── pregnancyStartDate: Timestamp?
  ├── estimatedDueDate: Timestamp?
  └── createdAt: Timestamp

/couples/{coupleId}/notes/{noteId}
  ├── id: String
  ├── authorId: String
  ├── date: Timestamp
  ├── category: String
  ├── mood: String
  ├── severityLevel: Number
  ├── content: String
  ├── husbandResponse: String?
  ├── husbandRespondedAt: Timestamp?
  └── updatedAt: Timestamp

/couples/{coupleId}/schedules/{scheduleId}
  ├── id: String
  ├── visitNumber: Number
  ├── title: String
  ├── scheduledDateTime: Timestamp
  ├── faskesName: String
  ├── doctorOrMidwifeName: String
  ├── isCompleted: Boolean
  ├── notes: String?
  ├── husbandChecklist: Map<String, Boolean>
  └── updatedAt: Timestamp

/learning_modules/{moduleId}
  ├── id: String
  ├── trimester: Number
  ├── title: String
  ├── summary: String
  ├── readTime: Number
  ├── content: String
  └── quiz: Map (Questions, Options, Explanations, CorrectAnswerIndex)
```

---

## 5. Pola Offline-First & Sinkronisasi (Room <-> Firestore)

1. **Prinsip Single Source of Truth**:
   - UI Jetpack Compose selalu mengamati data dari **Room Database** melalui `Flow<List<T>>`.
   - Hal ini menjamin aplikasi tetap responsif, langsung menampilkan data saat dibuka tanpa delay loading jaringan, dan berfungsi offline.
2. **Sinkronisasi Dua Arah**:
   - Saat pengguna membuat/mengubah data (misal: tambah catatan atau ubah jadwal), repository menyimpannya terlebih dahulu ke Room lokal dengan flag `syncStatus = PENDING`.
   - Worker/Coroutine Service kemudian mem-push data ke Firebase Firestore. Begitu sukses, status lokal diubah menjadi `SYNCED`.
   - Firestore `addSnapshotListener` secara real-time mendengarkan perubahan dari pasangan (misal: suami membaca catatan baru dari istri), dan langsung memperbarui Room database lokal. Room kemudian memicu emit data baru ke UI secara otomatis.
