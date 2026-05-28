package com.example.bromoindah.ui.screen.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bromoindah.domain.model.Pesanan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBackClick: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Pesanan") },
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
            } else if (uiState.bookings.isEmpty()) {
                Text(
                    text = "Belum ada riwayat pesanan",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.bookings) { pesanan ->
                        BookingItem(pesanan = pesanan)
                    }
                }
            }
        }
    }
}

@Composable
fun BookingItem(pesanan: Pesanan) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Tanggal: ${pesanan.tanggal_pesan}", style = MaterialTheme.typography.labelLarge)
                StatusChip(status = pesanan.status_pesanan)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Wisata ID: ${pesanan.wisata_id}", style = MaterialTheme.typography.titleMedium)
            Text(text = "${pesanan.jumlah_tiket} Tiket", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Total: Rp ${pesanan.total_harga}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val containerColor = when (status) {
        "Menunggu Pembayaran" -> Color(0xFFFFEB3B) // Yellow
        "Menunggu Konfirmasi" -> Color(0xFF2196F3) // Blue
        "Dikonfirmasi" -> Color(0xFF4CAF50) // Green
        "Selesai" -> Color(0xFF9E9E9E) // Grey
        "Dibatalkan" -> Color(0xFFF44336) // Red
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    
    Surface(
        color = containerColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = if (status == "Menunggu Pembayaran") Color.Black else Color.White
        )
    }
}
