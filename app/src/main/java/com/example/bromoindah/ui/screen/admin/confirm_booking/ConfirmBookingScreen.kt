package com.example.bromoindah.ui.screen.admin.confirm_booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import com.example.bromoindah.domain.model.Pesanan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmBookingScreen(
    onBackClick: () -> Unit,
    viewModel: ConfirmBookingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Konfirmasi Pesanan") },
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
                    text = "Tidak ada pesanan menunggu konfirmasi",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.bookings) { pesanan ->
                        ConfirmBookingItem(
                            pesanan = pesanan,
                            onApprove = { pesanan.id?.let { viewModel.updateStatus(it, "Dikonfirmasi") } },
                            onReject = { pesanan.id?.let { viewModel.updateStatus(it, "Dibatalkan") } }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmBookingItem(
    pesanan: Pesanan,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "User ID: ${pesanan.user_id}", style = MaterialTheme.typography.labelLarge)
            Text(text = "Tanggal: ${pesanan.tanggal_pesan}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Total: Rp ${pesanan.total_harga}", style = MaterialTheme.typography.titleMedium)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(text = "Bukti Pembayaran:", style = MaterialTheme.typography.labelMedium)
            AsyncImage(
                model = pesanan.bukti_bayar_url,
                contentDescription = "Bukti Bayar",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onApprove,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Terima")
                }
                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tolak")
                }
            }
        }
    }
}
