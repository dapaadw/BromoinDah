# Product Requirements Document
# BromoInDah — Aplikasi Sistem Pemesanan Tiket Tempat Wisata Malang

---
| Informasi | Detail |
|---|---|
| Versi Dokumen | 1.0 |
| Tanggal | 2026 |
| Status | Draft |
| Platform | Android (Jetpack Compose) |
| Backend / Database | Supabase (PostgreSQL + Auth + Storage) |
---

## Daftar Isi

1. [Pendahuluan](#1-pendahuluan)
2. [Deskripsi Produk](#2-deskripsi-produk)
3. [Fitur & Kebutuhan Fungsional](#3-fitur--kebutuhan-fungsional)
4. [Alur Aplikasi](#4-alur-aplikasi)
5. [Struktur Database](#5-struktur-database-supabasepostgresql)
6. [Arsitektur Aplikasi](#6-arsitektur-aplikasi)
7. [Kebutuhan Non-Fungsional](#7-kebutuhan-non-fungsional)
8. [Pembagian Tugas Pengembangan](#8-pembagian-tugas-pengembangan)
9. [Rencana Pengembangan](#9-rencana-pengembangan)
10. [Penutup](#10-penutup)

---

## 1. Pendahuluan

### 1.1. Latar Belakang

Malang Raya, yang meliputi Kota Malang, Kabupaten Malang, dan Kota Batu, telah berkembang menjadi episentrum pariwisata unggulan di Jawa Timur berkat keberagaman destinasi alam dan buatannya. Namun, tingginya minat wisatawan belum diimbangi dengan infrastruktur manajemen yang memadai, sehingga masalah antrian panjang serta sulitnya akses informasi tiket secara real-time masih sering terjadi.

BromoInDah hadir sebagai solusi strategis berbasis mobile yang memungkinkan wisatawan memesan tiket secara digital, mendapatkan validasi elektronik, dan mengeksplorasi berbagai destinasi wisata di Malang melalui satu platform terintegrasi.

### 1.2. Tujuan Dokumen

Dokumen PRD ini mendefinisikan secara lengkap kebutuhan fungsional dan non-fungsional untuk pengembangan aplikasi BromoInDah menggunakan teknologi Jetpack Compose (Android) dengan Supabase sebagai backend dan database. Dokumen ini menjadi acuan bagi seluruh anggota tim pengembang.

### 1.3. Ruang Lingkup

- Aplikasi mobile Android menggunakan Jetpack Compose
- Backend as a Service menggunakan Supabase (Auth, PostgreSQL, Storage, Realtime)
- Dua role pengguna: **User** (wisatawan) dan **Admin** (pengelola destinasi)
- Cakupan destinasi wisata di wilayah Malang Raya

### 1.4. Definisi & Singkatan

| Istilah | Definisi |
|---|---|
| PRD | Product Requirements Document |
| Supabase | Platform backend open-source berbasis PostgreSQL dengan fitur Auth, Storage, dan Realtime |
| Jetpack Compose | Toolkit UI deklaratif modern untuk pengembangan aplikasi Android |
| RLS | Row Level Security — mekanisme keamanan data di level baris pada PostgreSQL/Supabase |
| JWT | JSON Web Token — token autentikasi yang digunakan oleh Supabase Auth |
| CRUD | Create, Read, Update, Delete — operasi dasar pada data |
| Admin | Pengguna dengan role admin yang dapat mengelola data wisata dan konfirmasi pembayaran |
| User | Pengguna umum (wisatawan) yang dapat memesan tiket dan memberi ulasan |

---

## 2. Deskripsi Produk

### 2.1. Gambaran Umum Produk

BromoInDah adalah aplikasi mobile Android yang memungkinkan wisatawan untuk mencari, melihat detail, dan memesan tiket destinasi wisata di wilayah Malang Raya secara digital. Aplikasi ini dibangun menggunakan Jetpack Compose untuk UI dan Supabase sebagai backend terpadu (autentikasi, database, penyimpanan file, dan real-time updates).

### 2.2. Stack Teknologi

| Komponen | Teknologi | Keterangan |
|---|---|---|
| UI Framework | Jetpack Compose | Deklaratif, Material Design 3 |
| Bahasa Pemrograman | Kotlin | Versi terbaru yang kompatibel dengan Compose |
| Backend & Database | Supabase | PostgreSQL, Auth, Storage, Realtime |
| Autentikasi | Supabase Auth | Email/Password, JWT-based |
| Penyimpanan File | Supabase Storage | Foto wisata, foto profil, foto review |
| ORM / Query | Supabase Kotlin SDK | Wrapper resmi Supabase untuk Android |
| State Management | ViewModel + StateFlow | MVVM pattern |
| Navigasi | Jetpack Navigation Compose | Type-safe navigation |
| Dependency Injection | Hilt | Injeksi dependensi berbasis Dagger |
| Image Loading | Coil | Async image loading untuk Compose |

### 2.3. Role Pengguna

| Role | Deskripsi | Akses Fitur |
|---|---|---|
| User (Wisatawan) | Pengguna umum yang melakukan registrasi dan login | Lihat daftar & detail wisata, pesan tiket, riwayat pesanan, review |
| Admin | Pengelola destinasi / pengelola sistem | Semua fitur User + kelola data wisata + konfirmasi pembayaran |

---

## 3. Fitur & Kebutuhan Fungsional

### 3.1. Autentikasi (Login & Register)

Fitur autentikasi diimplementasikan menggunakan **Supabase Auth** dengan email dan password.

#### 3.1.1. Register

| ID | Kebutuhan Fungsional |
|---|---|
| F-01 | Pengguna dapat membuat akun baru dengan mengisi nama lengkap, email, dan password |
| F-02 | Sistem melakukan validasi format email dan panjang minimal password (min. 8 karakter) |
| F-03 | Supabase Auth membuat user baru dan menyimpan metadata (`nama_lengkap`, `role: 'user'`) ke tabel `users` melalui trigger database |
| F-04 | Setelah register berhasil, pengguna diarahkan ke halaman Login |

#### 3.1.2. Login

| ID | Kebutuhan Fungsional |
|---|---|
| F-05 | Pengguna login menggunakan email dan password |
| F-06 | Supabase Auth mengembalikan JWT yang disimpan secara lokal (SharedPreferences/DataStore) |
| F-07 | Sistem membaca kolom `role` dari tabel `users` untuk menentukan navigasi awal (Home User atau Dashboard Admin) |
| F-08 | Pengguna dapat logout, yang menghapus sesi JWT lokal dan sesi Supabase |

---

### 3.2. Daftar Wisata

| ID | Kebutuhan Fungsional |
|---|---|
| F-09 | Sistem menampilkan daftar semua destinasi wisata dalam bentuk `LazyColumn` (nama wisata, foto thumbnail, lokasi) |
| F-10 | Data diambil dari tabel `wisata` di Supabase secara real-time |
| F-11 | Terdapat fitur pencarian wisata berdasarkan nama atau lokasi |
| F-12 | Setiap item dapat di-tap untuk membuka halaman Detail Wisata |

---

### 3.3. Detail Wisata

| ID | Kebutuhan Fungsional |
|---|---|
| F-13 | Halaman menampilkan informasi lengkap: nama wisata, galeri foto (dari Supabase Storage), deskripsi, lokasi, harga tiket, jam operasional, fasilitas, dan aturan kunjungan |
| F-14 | Foto wisata dimuat secara asinkron menggunakan Coil dari URL Supabase Storage |
| F-15 | Terdapat tombol **"Pesan Tiket"** yang mengarahkan ke halaman Pemesanan |
| F-16 | Menampilkan rata-rata rating dan daftar ulasan dari pengguna lain (dari tabel `review`) |

---

### 3.4. Pemesanan Tiket

| ID | Kebutuhan Fungsional |
|---|---|
| F-17 | Pengguna memilih tanggal kunjungan menggunakan DatePicker Compose |
| F-18 | Pengguna menentukan jumlah tiket |
| F-19 | Sistem mengkalkulasi total harga secara otomatis (`jumlah_tiket × harga_tiket`) |
| F-20 | Pengguna mengkonfirmasi pesanan; sistem menyimpan record baru ke tabel `pesanan` dengan `status_pesanan = 'Menunggu Pembayaran'` |
| F-21 | Pengguna diarahkan untuk melakukan pembayaran (upload bukti transfer ke Supabase Storage) |
| F-22 | Setelah upload bukti, status berubah menjadi `'Menunggu Konfirmasi'` |

---

### 3.5. Riwayat Pesanan

| ID | Kebutuhan Fungsional |
|---|---|
| F-23 | Pengguna dapat melihat semua pesanan miliknya (filter berdasarkan `user_id` menggunakan RLS Supabase) |
| F-24 | Setiap item menampilkan nama wisata, tanggal kunjungan, jumlah tiket, total harga, dan status pesanan |
| F-25 | Status pesanan ditampilkan dalam chip berwarna: **Menunggu Pembayaran** (kuning), **Menunggu Konfirmasi** (biru), **Dikonfirmasi** (hijau), **Selesai** (abu-abu), **Dibatalkan** (merah) |
| F-26 | Pesanan dengan status `'Selesai'` memiliki tombol **"Beri Ulasan"** |

---

### 3.6. Review Pemesanan

| ID | Kebutuhan Fungsional |
|---|---|
| F-27 | Fitur aktif hanya untuk pesanan dengan status `'Selesai'` |
| F-28 | Pengguna memberikan rating bintang (1–5) menggunakan komponen RatingBar kustom di Compose |
| F-29 | Pengguna menulis ulasan teks (opsional) |
| F-30 | Pengguna dapat mengunggah foto review ke Supabase Storage (opsional) |
| F-31 | Sistem menyimpan data ke tabel `review`; satu pesanan hanya dapat direview satu kali |

---

### 3.7. Pengelolaan Data Wisata (Admin)

| ID | Kebutuhan Fungsional |
|---|---|
| F-32 | Admin dapat menambah destinasi wisata baru (nama, deskripsi, lokasi, harga tiket, jam operasional, fasilitas, aturan) |
| F-33 | Admin dapat mengunggah foto wisata ke Supabase Storage; URL foto disimpan di kolom `foto_wisata_url` |
| F-34 | Admin dapat memperbarui informasi wisata yang ada |
| F-35 | Admin dapat menghapus destinasi wisata |
| F-36 | Admin melihat daftar pesanan yang berstatus `'Menunggu Konfirmasi'` beserta bukti pembayaran |
| F-37 | Admin dapat mengkonfirmasi pembayaran (status → `'Dikonfirmasi'`) atau menolak (status → `'Dibatalkan'`) |

---

## 4. Alur Aplikasi

### 4.1. Alur Pengguna (User)

```
Buka Aplikasi
    └── Splash Screen → cek sesi JWT Supabase
            ├── Sesi tidak ada → Login / Register
            └── Sesi valid → cek role
                    └── role: 'user' → Home (Daftar Wisata)
                            └── Tap item wisata → Detail Wisata
                                    └── Tap "Pesan Tiket" → Pemesanan
                                            ├── Isi tanggal & jumlah tiket
                                            ├── Kalkulasi total harga
                                            └── Konfirmasi → pesanan tersimpan
                                                    (status: Menunggu Pembayaran)
                                                        └── Upload bukti bayar
                                                                (status: Menunggu Konfirmasi)
                                                                    └── Admin konfirmasi
                                                                            (status: Dikonfirmasi)
                                                                                └── Setelah kunjungan
                                                                                        (status: Selesai)
                                                                                            └── Beri Review
```

### 4.2. Alur Admin

```
Login → role: 'admin' → Dashboard Admin
    ├── Kelola Data Wisata
    │       ├── Tambah wisata baru + upload foto
    │       ├── Edit informasi wisata
    │       └── Hapus wisata
    └── Konfirmasi Pesanan
            ├── Lihat daftar pesanan "Menunggu Konfirmasi"
            ├── Lihat bukti pembayaran
            ├── Konfirmasi → status: Dikonfirmasi
            └── Update ke Selesai setelah kunjungan
```

### 4.3. Status Pesanan

```
Menunggu Pembayaran → Menunggu Konfirmasi → Dikonfirmasi → Selesai
                                          ↘
                                           Dibatalkan
```

---

## 5. Struktur Database (Supabase/PostgreSQL)

### 5.1. Tabel `users`

| Kolom | Tipe Data | Constraint | Keterangan |
|---|---|---|---|
| `id` | UUID | PRIMARY KEY, DEFAULT `gen_random_uuid()` | Foreign key ke `auth.users` Supabase |
| `nama_lengkap` | VARCHAR(100) | NOT NULL | Nama lengkap pengguna |
| `email` | VARCHAR(100) | NOT NULL, UNIQUE | Email pengguna |
| `foto_profil_url` | VARCHAR(255) | NULLABLE | URL foto profil di Supabase Storage |
| `role` | ENUM('user','admin') | NOT NULL, DEFAULT 'user' | Role pengguna |
| `created_at` | TIMESTAMP | DEFAULT `now()` | Waktu pembuatan akun |

> **Catatan:** Password tidak disimpan di tabel ini. Autentikasi sepenuhnya dikelola oleh Supabase Auth (`auth.users`).

---

### 5.2. Tabel `wisata`

| Kolom | Tipe Data | Constraint | Keterangan |
|---|---|---|---|
| `id` | UUID / SERIAL | PRIMARY KEY | Identifikasi unik destinasi |
| `nama_wisata` | VARCHAR(150) | NOT NULL | Nama destinasi wisata |
| `deskripsi` | TEXT | NOT NULL | Deskripsi lengkap wisata |
| `lokasi` | VARCHAR(200) | NOT NULL | Nama lokasi / alamat |
| `harga_tiket` | INTEGER | NOT NULL | Harga tiket dalam Rupiah |
| `jam_operasional` | VARCHAR(100) | NULLABLE | Contoh: `07:00 - 17:00 WIB` |
| `fasilitas` | TEXT | NULLABLE | Daftar fasilitas tersedia |
| `aturan_kunjungan` | TEXT | NULLABLE | Aturan yang berlaku |
| `foto_wisata_url` | VARCHAR(255) | NULLABLE | URL utama foto wisata di Storage |
| `created_at` | TIMESTAMP | DEFAULT `now()` | Waktu data ditambahkan |

---

### 5.3. Tabel `pesanan`

| Kolom | Tipe Data | Constraint | Keterangan |
|---|---|---|---|
| `id` | UUID / SERIAL | PRIMARY KEY | Identifikasi unik pesanan |
| `user_id` | UUID | FK → `users(id)` | Pengguna yang memesan |
| `wisata_id` | UUID / INTEGER | FK → `wisata(id)` | Destinasi yang dipesan |
| `tanggal_pesan` | DATE | NOT NULL | Tanggal kunjungan yang dipilih |
| `jumlah_tiket` | INTEGER | NOT NULL, CHECK > 0 | Jumlah tiket yang dipesan |
| `total_harga` | INTEGER | NOT NULL | Total harga (`jumlah_tiket × harga_tiket`) |
| `bukti_bayar_url` | VARCHAR(255) | NULLABLE | URL bukti transfer di Supabase Storage |
| `status_pesanan` | ENUM | NOT NULL, DEFAULT 'Menunggu Pembayaran' | Menunggu Pembayaran \| Menunggu Konfirmasi \| Dikonfirmasi \| Selesai \| Dibatalkan |
| `created_at` | TIMESTAMP | DEFAULT `now()` | Waktu pesanan dibuat |

---

### 5.4. Tabel `review`

| Kolom | Tipe Data | Constraint | Keterangan |
|---|---|---|---|
| `id` | UUID / SERIAL | PRIMARY KEY | Identifikasi unik review |
| `user_id` | UUID | FK → `users(id)` | Pengguna yang memberikan review |
| `wisata_id` | UUID / INTEGER | FK → `wisata(id)` | Destinasi yang direview |
| `pesanan_id` | UUID / INTEGER | FK → `pesanan(id)`, UNIQUE | Satu pesanan = satu review |
| `rating` | INTEGER | NOT NULL, CHECK 1–5 | Nilai rating bintang |
| `ulasan` | TEXT | NULLABLE | Ulasan teks dari pengguna |
| `foto_review_url` | VARCHAR(255) | NULLABLE | URL foto review di Storage |
| `created_at` | TIMESTAMP | DEFAULT `now()` | Waktu review dibuat |

---

### 5.5. Row Level Security (RLS) Supabase

RLS **diaktifkan** pada semua tabel untuk memastikan keamanan data di level baris.

| Tabel | Policy | Keterangan |
|---|---|---|
| `users` | SELECT own row | Pengguna hanya bisa membaca datanya sendiri; Admin bisa membaca semua |
| `wisata` | SELECT all; INSERT/UPDATE/DELETE admin only | Semua bisa membaca; hanya admin yang bisa modifikasi |
| `pesanan` | SELECT/INSERT own; UPDATE admin | User hanya bisa membaca & membuat pesanannya sendiri; Admin bisa update status |
| `review` | SELECT all; INSERT own (pesanan selesai) | Semua bisa membaca; user hanya bisa insert review untuk pesanannya yang selesai |

### 5.6. Database Trigger

```sql
-- Trigger: insert ke tabel users setelah registrasi Supabase Auth
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS TRIGGER AS $$
BEGIN
  INSERT INTO public.users (id, nama_lengkap, email, role)
  VALUES (
    NEW.id,
    NEW.raw_user_meta_data->>'nama_lengkap',
    NEW.email,
    'user'
  );
  RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

CREATE TRIGGER on_auth_user_created
  AFTER INSERT ON auth.users
  FOR EACH ROW EXECUTE FUNCTION public.handle_new_user();
```

---

## 6. Arsitektur Aplikasi

### 6.1. Pola Arsitektur: MVVM + Clean Architecture

| Layer | Komponen | Tanggung Jawab |
|---|---|---|
| Presentation | Composable Screens, ViewModel, StateFlow/State | Menampilkan UI, menangani input pengguna, observe state |
| Domain | Use Cases, Repository Interface | Business logic, validasi aturan bisnis |
| Data | Repository Impl, Supabase Client, DTO/Entity | Komunikasi dengan Supabase SDK, mapping data |
| DI | Hilt Modules | Injeksi dependensi SupabaseClient, Repository, ViewModel |

### 6.2. Struktur Package

```
com.bromo.indah/
├── ui/
│   ├── screens/          # Composable screens (HomeScreen, DetailScreen, dll.)
│   ├── components/       # Reusable composable components
│   └── theme/            # Material Design 3 theme, color, typography
├── viewmodel/            # ViewModel per fitur
├── domain/
│   ├── usecase/          # Business logic use cases
│   ├── model/            # Domain models / entities
│   └── repository/       # Repository interfaces
├── data/
│   ├── repository/       # Repository implementations
│   └── remote/           # Supabase client setup & DTO
├── di/                   # Hilt modules
└── util/                 # Extensions, helpers, constants
```

### 6.3. Navigasi

Menggunakan **Jetpack Navigation Compose** dengan NavHost tunggal. Route dikelompokkan menjadi:

- **Auth Graph:** `splash`, `login`, `register`
- **User Graph:** `home`, `detail/{wisataId}`, `pemesanan/{wisataId}`, `riwayat`, `review/{pesananId}`, `profil`
- **Admin Graph:** `adminDashboard`, `kelolaWisata`, `konfirmasiPesanan`

### 6.4. Setup Supabase Client (Kotlin)

```kotlin
// build.gradle.kts (app)
implementation("io.github.jan-tennert.supabase:postgrest-kt:2.x.x")
implementation("io.github.jan-tennert.supabase:auth-kt:2.x.x")
implementation("io.github.jan-tennert.supabase:storage-kt:2.x.x")
implementation("io.github.jan-tennert.supabase:realtime-kt:2.x.x")
implementation("io.ktor:ktor-client-android:2.x.x")

// SupabaseModule.kt (Hilt)
@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {
    @Provides @Singleton
    fun provideSupabaseClient(): SupabaseClient = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
        install(Realtime)
    }
}
```

---

## 7. Kebutuhan Non-Fungsional

| Kategori | Kebutuhan |
|---|---|
| **Performa** | Halaman daftar wisata harus termuat dalam < 3 detik pada koneksi 4G; query Supabase dioptimalkan dengan indexing pada kolom `wisata_id` dan `user_id` |
| **Keamanan** | Semua request ke Supabase menggunakan JWT; RLS diaktifkan pada semua tabel; password tidak disimpan di aplikasi; data sensitif tidak di-log |
| **Skalabilitas** | Arsitektur MVVM memudahkan penambahan fitur baru; Supabase mendukung scaling otomatis |
| **Usability** | Mengikuti panduan Material Design 3; UI responsif untuk berbagai ukuran layar Android; feedback loading state (shimmer / `CircularProgressIndicator`) pada semua operasi async |
| **Reliabilitas** | Penanganan error jaringan dengan pesan informatif; mekanisme retry untuk operasi Supabase yang gagal; offline state handling dengan pesan yang jelas |
| **Maintainability** | Kode mengikuti Kotlin Coding Conventions; setiap ViewModel memiliki unit test; penggunaan Hilt memudahkan pengujian dengan mock |
| **Kompatibilitas** | Minimum SDK: Android 8.0 (API 26); Target SDK: Android 14 (API 34) |

---

## 8. Pembagian Tugas Pengembangan

| Nama | NIM | Modul | Detail Tanggung Jawab |
|---|---|---|---|
| Abbyan Ezra Yudhistira | 245150700111030 | Pemesanan Tiket & Pengelolaan Data Wisata (Admin) | `PemesananScreen`, kalkulasi harga, integrasi tabel `pesanan` (insert & update); Admin: CRUD wisata, upload foto ke Supabase Storage, dashboard konfirmasi pembayaran; Hilt module untuk repository wisata & pesanan |
| Daffa Dwika Anargya | 245150700111035 | Riwayat Pesanan & Review | `RiwayatPesananScreen` dengan filter status; `ReviewScreen` dengan RatingBar kustom & upload foto review; `RiwayatViewModel` & `ReviewViewModel`; integrasi tabel `pesanan` (SELECT) dan `review` (INSERT); chip status berwarna |
| Mukhamad Irfan Nur Khakim | 245150701111021 | Detail Wisata | `DetailWisataScreen` dengan galeri foto (Coil + Supabase Storage), informasi lengkap, rata-rata rating; navigasi dari Daftar → Detail dan Detail → Pemesanan; `DetailViewModel` |
| Muhammad Farhan Muzakkiy | 245150707111048 | Daftar Wisata | `HomeScreen` dengan `LazyColumn`, search bar, thumbnail foto; `HomeViewModel` dengan StateFlow; integrasi real-time Supabase (subscribe channel wisata); navigasi ke `DetailScreen` |
| Semua Anggota | — | Login & Register | `SplashScreen` (cek sesi JWT), `LoginScreen`, `RegisterScreen`; Supabase Auth integration (`signUp`, `signIn`, `signOut`); database trigger untuk insert ke tabel `users`; navigasi berdasarkan role; penyimpanan sesi dengan DataStore |


## 9. Penutup

Dokumen PRD ini telah mendefinisikan secara komprehensif seluruh kebutuhan fungsional dan non-fungsional aplikasi BromoInDah. Dengan memanfaatkan Jetpack Compose untuk antarmuka modern dan Supabase sebagai backend terpadu, tim diharapkan dapat membangun aplikasi yang efisien, aman, dan mudah dipelihara.

Aplikasi ini diharapkan menjadi solusi nyata untuk mendigitalisasi ekosistem pariwisata Malang Raya, memberikan kemudahan bagi wisatawan, dan mendukung pertumbuhan ekonomi lokal berbasis teknologi.

Dokumen ini bersifat *living document* dan dapat diperbarui sesuai dengan kebutuhan yang berkembang selama proses pengembangan.

---

*Disusun oleh Tim BromoInDah — Program Studi Teknologi Informasi, Fakultas Ilmu Komputer, Universitas Brawijaya, 2026*

*Diperiksa oleh: Djoko Pramono, S.T., M.Kom. — Dosen Pengampu Mata Kuliah Pengembangan Aplikasi Mobile*