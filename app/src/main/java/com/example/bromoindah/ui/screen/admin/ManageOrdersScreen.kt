package com.example.bromoindah.ui.screen.admin

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bromoindah.data.model.Pesanan
import com.example.bromoindah.theme.StatusCancelled
import com.example.bromoindah.theme.StatusCompleted
import com.example.bromoindah.theme.StatusConfirmed
import com.example.bromoindah.theme.StatusPending
import com.example.bromoindah.ui.viewmodel.PesananViewModel
import kotlinx.coroutines.launch

private fun formatRupiah(amount: Int): String {
    val format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID"))
    return format.format(amount)
}

private fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "pending" -> StatusPending
        "confirmed" -> StatusConfirmed
        "completed" -> StatusCompleted
        "cancelled" -> StatusCancelled
        else -> StatusPending
    }
}

private fun getStatusLabel(status: String): String {
    return when (status.lowercase()) {
        "pending" -> "Menunggu"
        "confirmed" -> "Dikonfirmasi"
        "completed" -> "Selesai"
        "cancelled" -> "Dibatalkan"
        else -> status
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageOrdersScreen(
    onNavigateBack: () -> Unit,
    pesananViewModel: PesananViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by pesananViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var selectedFilter by remember { mutableStateOf("Semua") }
    var showStatusDialog by remember { mutableStateOf(false) }
    var selectedPesanan by remember { mutableStateOf<Pesanan?>(null) }
    var targetStatus by remember { mutableStateOf("") }
    var statusActionLabel by remember { mutableStateOf("") }

    val filters = listOf("Semua", "Menunggu", "Dikonfirmasi", "Selesai")

    LaunchedEffect(Unit) {
        pesananViewModel.loadAllPesanan()
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            scope.launch {
                snackbarHostState.showSnackbar(error)
                pesananViewModel.clearError()
            }
        }
    }

    val filteredOrders = remember(uiState.allPesanan, selectedFilter) {
        when (selectedFilter) {
            "Menunggu" -> uiState.allPesanan.filter { it.status == "pending" }
            "Dikonfirmasi" -> uiState.allPesanan.filter { it.status == "confirmed" }
            "Selesai" -> uiState.allPesanan.filter { it.status == "completed" }
            else -> uiState.allPesanan
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kelola Pesanan",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading && uiState.allPesanan.isEmpty()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (filteredOrders.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Receipt,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tidak ada pesanan",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredOrders, key = { it.id.orEmpty() }) { pesanan ->
                            OrderManageItem(
                                pesanan = pesanan,
                                onConfirm = {
                                    selectedPesanan = pesanan
                                    targetStatus = "confirmed"
                                    statusActionLabel = "mengonfirmasi"
                                    showStatusDialog = true
                                },
                                onComplete = {
                                    selectedPesanan = pesanan
                                    targetStatus = "completed"
                                    statusActionLabel = "menyelesaikan"
                                    showStatusDialog = true
                                },
                                onCancel = {
                                    selectedPesanan = pesanan
                                    targetStatus = "cancelled"
                                    statusActionLabel = "membatalkan"
                                    showStatusDialog = true
                                }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                }
            }
        }
    }

    // Status change confirmation dialog
    if (showStatusDialog && selectedPesanan != null) {
        AlertDialog(
            onDismissRequest = {
                showStatusDialog = false
                selectedPesanan = null
            },
            title = {
                Text(
                    text = "Ubah Status Pesanan",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Apakah Anda yakin ingin $statusActionLabel pesanan ini?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedPesanan?.let {
                            pesananViewModel.updateStatus(it.id.orEmpty(), targetStatus)
                        }
                        showStatusDialog = false
                        selectedPesanan = null
                    }
                ) {
                    Text(
                        text = "Ya",
                        color = if (targetStatus == "cancelled") {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showStatusDialog = false
                        selectedPesanan = null
                    }
                ) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
private fun OrderManageItem(
    pesanan: Pesanan,
    onConfirm: () -> Unit,
    onComplete: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusColor = getStatusColor(pesanan.status)
    val statusLabel = getStatusLabel(pesanan.status)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pesanan.wisata?.nama ?: "Wisata",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = statusLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = statusColor
                    ),
                    border = null
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Tanggal: ${pesanan.tanggalKunjungan}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Jumlah tiket: ${pesanan.jumlahTiket}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = formatRupiah(pesanan.totalHarga),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Action buttons based on status
            if (pesanan.status == "pending" || pesanan.status == "confirmed") {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = StatusCancelled
                        )
                    ) {
                        Text("Batalkan", style = MaterialTheme.typography.labelMedium)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (pesanan.status == "pending") {
                        Button(
                            onClick = onConfirm,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StatusConfirmed
                            )
                        ) {
                            Text("Konfirmasi", style = MaterialTheme.typography.labelMedium)
                        }
                    } else if (pesanan.status == "confirmed") {
                        Button(
                            onClick = onComplete,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StatusCompleted
                            )
                        ) {
                            Text("Selesaikan", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }
}
