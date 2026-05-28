package com.example.bromoindah.ui.screen.booking

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    onBackClick: () -> Unit,
    onBookingSuccess: () -> Unit,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onBookingSuccess()
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        // Simple formatting for now
                        val date = java.util.Date(it)
                        val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                        viewModel.onDateSelected(format.format(date))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pemesanan Tiket") },
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
        ) {
            uiState.wisata?.let { wisata ->
                Text(text = wisata.nama_wisata, style = MaterialTheme.typography.headlineSmall)
                Text(text = "Harga per tiket: Rp ${wisata.harga_tiket}", style = MaterialTheme.typography.bodyMedium)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (uiState.selectedDate.isEmpty()) "Pilih Tanggal Kunjungan" else "Tanggal: ${uiState.selectedDate}")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(text = "Jumlah Tiket", style = MaterialTheme.typography.titleMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    IconButton(onClick = { if (uiState.jumlahTiket > 1) viewModel.onJumlahTiketChanged(uiState.jumlahTiket - 1) }) {
                        Text("-", style = MaterialTheme.typography.headlineMedium)
                    }
                    Text(
                        text = uiState.jumlahTiket.toString(),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(onClick = { viewModel.onJumlahTiketChanged(uiState.jumlahTiket + 1) }) {
                        Text("+", style = MaterialTheme.typography.headlineMedium)
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Total Pembayaran", style = MaterialTheme.typography.labelLarge)
                            Text(text = "Rp ${uiState.totalHarga}", style = MaterialTheme.typography.titleLarge)
                        }
                        Button(
                            onClick = viewModel::createBooking,
                            enabled = uiState.selectedDate.isNotEmpty() && !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            } else {
                                Text("Pesan")
                            }
                        }
                    }
                }
            }
            
            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
