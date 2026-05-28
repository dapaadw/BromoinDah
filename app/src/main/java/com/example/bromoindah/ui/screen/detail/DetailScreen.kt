package com.example.bromoindah.ui.screen.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    onBookingClick: (String) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.wisata?.nama_wisata ?: "Detail Wisata") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                uiState.wisata?.let { wisata ->
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = wisata.foto_wisata_url,
                            contentDescription = wisata.nama_wisata,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = wisata.nama_wisata, style = MaterialTheme.typography.headlineMedium)
                            Text(text = wisata.lokasi, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(text = "Deskripsi", style = MaterialTheme.typography.titleLarge)
                            Text(text = wisata.deskripsi, style = MaterialTheme.typography.bodyLarge)
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(text = "Harga Tiket", style = MaterialTheme.typography.titleLarge)
                            Text(text = "Rp ${wisata.harga_tiket}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                            
                            if (wisata.jam_operasional != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(text = "Jam Operasional", style = MaterialTheme.typography.titleLarge)
                                Text(text = wisata.jam_operasional, style = MaterialTheme.typography.bodyLarge)
                            }

                            if (wisata.fasilitas != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(text = "Fasilitas", style = MaterialTheme.typography.titleLarge)
                                Text(text = wisata.fasilitas, style = MaterialTheme.typography.bodyLarge)
                            }
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            Button(
                                onClick = { wisata.id?.let { onBookingClick(it) } },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Pesan Tiket Sekarang")
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}
