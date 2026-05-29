-- SQL Script for seeding dummy data to Supabase
-- Copy and paste this script into your Supabase project's SQL Editor and click "Run".

INSERT INTO wisata (
    nama_wisata, 
    deskripsi, 
    lokasi, 
    harga_tiket, 
    jam_operasional, 
    fasilitas, 
    aturan_kunjungan, 
    foto_wisata_url
) VALUES 
(
    'Kawah Bromo', 
    'Kawah aktif Gunung Bromo yang menawarkan pemandangan spektakuler dengan asap belerang yang mengepul. Pengunjung dapat menaiki tangga menuju bibir kawah untuk melihat fenomena alam ini dari dekat.', 
    'Taman Nasional Bromo Tengger Semeru', 
    35000, 
    '00:00 - 18:00', 
    'Area parkir, toilet umum, penyewaan kuda, warung kecil', 
    'Gunakan masker karena bau belerang cukup kuat, jangan melewati batas pagar aman kawah.', 
    'https://images.unsplash.com/photo-1554481923-a6918bd997bc?auto=format&fit=crop&q=80&w=800'
),
(
    'Pasir Berbisik', 
    'Lautan pasir hitam yang sangat luas membentang di kaldera Bromo. Dinamakan demikian karena suara angin yang bertiup membawa butiran pasir terdengar seperti bisikan.', 
    'Taman Nasional Bromo Tengger Semeru', 
    25000, 
    '24 Jam', 
    'Penyewaan mobil Jeep (Hardtop), spot fotografi, penyewaan kuda', 
    'Wajib menggunakan kacamata dan masker pelindung wajah agar debu pasir tidak masuk ke mata dan pernapasan.', 
    'https://images.unsplash.com/photo-1588668214407-6ea9a6d8c272?auto=format&fit=crop&q=80&w=800'
),
(
    'Bukit Teletubbies (Savana)', 
    'Padang sabana hijau yang luas dengan bukit-bukit kecil bergelombang yang menyerupai pemandangan di serial anak Teletubbies. Sangat indah terutama setelah musim hujan.', 
    'Bagian Selatan Gunung Bromo', 
    25000, 
    '05:00 - 17:00', 
    'Spot foto, warung makan ringan, toilet, area istirahat', 
    'Dilarang membuang sampah sembarangan dan jangan memetik atau merusak tanaman liar di savana.', 
    'https://images.unsplash.com/photo-1505993597083-3bd19fd85e65?auto=format&fit=crop&q=80&w=800'
),
(
    'Penanjakan 1', 
    'Spot terbaik dan tertinggi untuk menikmati pemandangan matahari terbit (sunrise) yang sangat terkenal dengan latar depan Gunung Bromo, Batok, dan Gunung Semeru di kejauhan.', 
    'Gunung Penanjakan', 
    40000, 
    '02:00 - 10:00', 
    'Warung kopi, musala, penyewaan jaket hangat, toilet, tribun penonton', 
    'Datang lebih awal (jam 3 pagi) untuk mendapatkan tempat yang bagus. Bawa jaket tebal karena suhu sangat dingin (bisa mencapai 5-10 derajat celcius).', 
    'https://images.unsplash.com/photo-1510340332306-03f443e263ab?auto=format&fit=crop&q=80&w=800'
),
(
    'Pura Luhur Poten', 
    'Pura suci tempat peribadatan umat Hindu Tengger. Terletak tepat di bawah kaki Gunung Bromo di tengah hamparan lautan pasir hitam.', 
    'Lautan Pasir Gunung Bromo', 
    0, 
    '24 Jam (Akses terbatas, diutamakan untuk ibadah)', 
    'Tempat ibadah', 
    'Berpakaian sopan dan menghargai umat yang beribadah. Wisatawan dilarang masuk ke area suci (utama mandala) tanpa izin khusus.', 
    'https://images.unsplash.com/photo-1518002054494-3a6f94352e9d?auto=format&fit=crop&q=80&w=800'
);
