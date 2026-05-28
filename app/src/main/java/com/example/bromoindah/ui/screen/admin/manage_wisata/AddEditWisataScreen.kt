package com.example.bromoindah.ui.screen.admin.manage_wisata

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditWisataScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: AddEditWisataViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.id == null) "Tambah Wisata" else "Edit Wisata") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.nama,
                onValueChange = viewModel::onNamaChanged,
                label = { Text("Nama Wisata") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            OutlinedTextField(
                value = uiState.deskripsi,
                onValueChange = viewModel::onDeskripsiChanged,
                label = { Text("Deskripsi") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                minLines = 3
            )

            OutlinedTextField(
                value = uiState.lokasi,
                onValueChange = viewModel::onLokasiChanged,
                label = { Text("Lokasi") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            OutlinedTextField(
                value = uiState.harga,
                onValueChange = viewModel::onHargaChanged,
                label = { Text("Harga Tiket") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = uiState.jamOperasional,
                onValueChange = viewModel::onJamOperasionalChanged,
                label = { Text("Jam Operasional") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            OutlinedTextField(
                value = uiState.fasilitas,
                onValueChange = viewModel::onFasilitasChanged,
                label = { Text("Fasilitas") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            OutlinedTextField(
                value = uiState.aturan,
                onValueChange = viewModel::onAturanChanged,
                label = { Text("Aturan Kunjungan") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            if (uiState.error != null) {
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = viewModel::saveWisata,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Simpan")
                }
            }
        }
    }
}
