package com.example.bromoindah.ui.screen.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.bromoindah.data.model.Wisata
import com.example.bromoindah.ui.viewmodel.WisataViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditWisataScreen(
    wisataId: String?,
    onNavigateBack: () -> Unit,
    wisataViewModel: WisataViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by wisataViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val isEditMode = wisataId != null

    var nama by rememberSaveable { mutableStateOf("") }
    var lokasi by rememberSaveable { mutableStateOf("") }
    var deskripsi by rememberSaveable { mutableStateOf("") }
    var hargaTiket by rememberSaveable { mutableStateOf("") }
    var jamOperasional by rememberSaveable { mutableStateOf("") }
    var fasilitas by rememberSaveable { mutableStateOf("") }
    var aturan by rememberSaveable { mutableStateOf("") }
    var imageUrl by rememberSaveable { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var dataLoaded by remember { mutableStateOf(false) }

    // Validation errors
    var namaError by remember { mutableStateOf(false) }
    var lokasiError by remember { mutableStateOf(false) }
    var hargaError by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    // Load existing data when editing
    LaunchedEffect(wisataId) {
        if (isEditMode && !dataLoaded) {
            wisataViewModel.loadWisataDetail(wisataId!!)
        }
    }

    // Pre-fill fields when data is loaded
    LaunchedEffect(uiState.selectedWisata) {
        if (isEditMode && !dataLoaded && uiState.selectedWisata != null) {
            val wisata = uiState.selectedWisata!!
            if (wisata.id == wisataId) {
                nama = wisata.nama
                lokasi = wisata.lokasi
                deskripsi = wisata.deskripsi
                hargaTiket = wisata.hargaTiket.toString()
                jamOperasional = wisata.jamOperasional
                fasilitas = wisata.fasilitas
                aturan = wisata.aturan
                imageUrl = wisata.imageUrl
                dataLoaded = true
            }
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            scope.launch {
                snackbarHostState.showSnackbar(error)
                wisataViewModel.clearError()
            }
            isSaving = false
            isUploading = false
        }
    }

    fun validateAndSave() {
        namaError = nama.isBlank()
        lokasiError = lokasi.isBlank()
        hargaError = hargaTiket.isBlank() || hargaTiket.toIntOrNull() == null

        if (namaError || lokasiError || hargaError) {
            scope.launch {
                snackbarHostState.showSnackbar("Harap lengkapi semua field yang wajib diisi")
            }
            return
        }

        isSaving = true

        fun doSave() {
            val wisata = Wisata(
                id = wisataId ?: "",
                nama = nama.trim(),
                lokasi = lokasi.trim(),
                deskripsi = deskripsi.trim(),
                hargaTiket = hargaTiket.toIntOrNull() ?: 0,
                jamOperasional = jamOperasional.trim(),
                fasilitas = fasilitas.trim(),
                aturan = aturan.trim(),
                imageUrl = imageUrl
            )

            if (isEditMode) {
                wisataViewModel.updateWisata(wisata)
            } else {
                wisataViewModel.addWisata(wisata)
            }
            onNavigateBack()
        }

        // Upload image first if selected
        selectedImageUri?.let { uri ->
            isUploading = true
            val bytes = context.contentResolver.openInputStream(uri)?.readBytes()
            if (bytes != null) {
                val fileName = "wisata_${UUID.randomUUID()}.jpg"
                wisataViewModel.uploadImage(fileName, bytes) { url ->
                    isUploading = false
                    imageUrl = url ?: imageUrl
                    doSave()
                }
            } else {
                isUploading = false
                doSave()
            }
        } ?: doSave()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) "Edit Wisata" else "Tambah Wisata",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { innerPadding ->
        if (isEditMode && uiState.isLoading && !dataLoaded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                // Image upload section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        selectedImageUri != null -> {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Gambar wisata",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        imageUrl.isNotEmpty() -> {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Gambar wisata",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        else -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CloudUpload,
                                    contentDescription = "Unggah gambar",
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tekan untuk memilih gambar",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    if (isUploading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Nama Wisata
                OutlinedTextField(
                    value = nama,
                    onValueChange = {
                        nama = it
                        namaError = false
                    },
                    label = { Text("Nama Wisata *") },
                    placeholder = { Text("Masukkan nama wisata") },
                    isError = namaError,
                    supportingText = if (namaError) {
                        { Text("Nama wisata wajib diisi") }
                    } else null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Lokasi
                OutlinedTextField(
                    value = lokasi,
                    onValueChange = {
                        lokasi = it
                        lokasiError = false
                    },
                    label = { Text("Lokasi *") },
                    placeholder = { Text("Masukkan lokasi wisata") },
                    isError = lokasiError,
                    supportingText = if (lokasiError) {
                        { Text("Lokasi wajib diisi") }
                    } else null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Deskripsi
                OutlinedTextField(
                    value = deskripsi,
                    onValueChange = { deskripsi = it },
                    label = { Text("Deskripsi") },
                    placeholder = { Text("Masukkan deskripsi wisata") },
                    minLines = 4,
                    maxLines = 6,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Harga Tiket
                OutlinedTextField(
                    value = hargaTiket,
                    onValueChange = {
                        hargaTiket = it.filter { c -> c.isDigit() }
                        hargaError = false
                    },
                    label = { Text("Harga Tiket (Rp) *") },
                    placeholder = { Text("Masukkan harga tiket") },
                    isError = hargaError,
                    supportingText = if (hargaError) {
                        { Text("Harga tiket wajib diisi dengan angka") }
                    } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Jam Operasional
                OutlinedTextField(
                    value = jamOperasional,
                    onValueChange = { jamOperasional = it },
                    label = { Text("Jam Operasional") },
                    placeholder = { Text("Contoh: 08:00 - 17:00") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Fasilitas
                OutlinedTextField(
                    value = fasilitas,
                    onValueChange = { fasilitas = it },
                    label = { Text("Fasilitas") },
                    placeholder = { Text("Masukkan fasilitas yang tersedia") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Aturan Kunjungan
                OutlinedTextField(
                    value = aturan,
                    onValueChange = { aturan = it },
                    label = { Text("Aturan Kunjungan") },
                    placeholder = { Text("Masukkan aturan kunjungan") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Save button
                Button(
                    onClick = { validateAndSave() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = !isSaving && !isUploading,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isSaving || isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Simpan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
