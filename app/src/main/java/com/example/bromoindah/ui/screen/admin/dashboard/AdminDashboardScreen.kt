package com.example.bromoindah.ui.screen.admin.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onNavigateToManageWisata: () -> Unit,
    onNavigateToConfirmBooking: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Admin Dashboard") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AdminMenuCard(
                title = "Kelola Wisata",
                description = "Tambah, edit, atau hapus destinasi wisata",
                icon = Icons.Default.Settings,
                onClick = onNavigateToManageWisata
            )
            
            AdminMenuCard(
                title = "Konfirmasi Pesanan",
                description = "Lihat bukti bayar dan konfirmasi tiket",
                icon = Icons.Default.List,
                onClick = onNavigateToConfirmBooking
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kembali ke Beranda")
            }
        }
    }
}

@Composable
fun AdminMenuCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                Text(text = description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
