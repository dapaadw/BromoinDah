# BromoInDah

BromoInDah is a modern Android application designed to streamline tourism ticket bookings in the Malang Raya region, including the iconic Mount Bromo. Built with **Jetpack Compose** and **Supabase**, it provides a seamless experience for tourists to discover destinations and for admins to manage bookings.

## Features

### User Side
- **Explore Destinations:** Browse a list of top tourism spots in Malang with real-time ratings.
- **Dynamic Ratings:** View average ratings and reviews fetched directly from the community.
- **Smart Booking:** Book tickets with a dynamic date picker.
- **Reviews:** Share your experience by giving ratings and uploading review photos.

### Admin Side
- **Dashboard:** Overview of management tools with a consistent brand theme.
- **Manage Wisata:** Add, edit, or remove tourism destinations.
- **Booking Confirmation:** Verify payment proofs and confirm or reject user bookings.

## Tech Stack
- **UI:** Jetpack Compose (Material 3)
- **Backend/DB:** Supabase (PostgreSQL, Auth, Storage)
- **Dependency Injection:** Hilt
- **Image Loading:** Coil 3
- **Networking:** Ktor / Supabase-kt
- **Concurrency:** Kotlin Coroutines & Flow

---

## Installation Guide

Follow these steps to get the project running on your local machine.

### 1. Prerequisites
- **Android Studio** (Ladybug or newer recommended)
- **Java 17+**
- A **Supabase** account 

### 2. Clone the Repository
```bash
git clone https://github.com/dapaadw/BromoinDah.git
cd BromoinDah
```

### 3. Supabase Configuration
1. Create a new project in your [Supabase Dashboard](https://supabase.com/).
2. Go to **Project Settings > API** and copy your `Project URL` and `Anon Key`.
3. Set up the following tables in the SQL Editor:
    - `users`: (id, nama_lengkap, email, role, etc.)
    - `wisata`: (id, nama_wisata, deskripsi, lokasi, harga_tiket, etc.)
    - `pesanan`: (id, user_id, wisata_id, tanggal_pesan, status_pesanan, etc.)
    - `review`: (id, wisata_id, user_id, rating, ulasan, etc.)
4. Create a storage bucket named `wisata-images` and `bukti-pembayaran` and set them to **Public**.

### 4. Setup Environment Variables
Create a file named `secrets.properties` in your root directory (if not already present) and add your Supabase credentials:
```properties
SUPABASE_URL=https://your-project-id.supabase.co
SUPABASE_KEY=your-anon-key-here
```

### 5. Build and Run
1. Open the project in **Android Studio**.
2. Sync Project with Gradle Files.
3. Select your emulator or physical device.
4. Click **Run**.

---

## Design Theme
The app uses a signature **Primary Green (#0A4D3C)** and **Secondary Green (#10B981)** theme to reflect the natural beauty of Malang's landscapes.

## License
This project is for educational purposes as a tourism management system.
