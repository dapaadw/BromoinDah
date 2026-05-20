package com.example.bromoindah.ui.screen.booking

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bromoindah.ui.components.formatRupiah
import com.example.bromoindah.ui.viewmodel.PesananViewModel
import java.util.Calendar

// Theme colors matching the green/nature brand
private val ThemeDarkGreen = Color(0xFF1B5E20)
private val ThemeMediumGreen = Color(0xFF2E7D32)
private val ThemeLightGreen = Color(0xFFE8F5E9)
private val ThemeOrange = Color(0xFFFF9800)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    wisataId: String,
    wisataNama: String,
    wisataHargaTiket: Int,
    wisataImageUrl: String,
    userId: String,
    onNavigateBack: () -> Unit,
    onPaymentNavigate: (pesananId: String, totalHarga: Int) -> Unit,
    pesananViewModel: PesananViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by pesananViewModel.uiState.collectAsState()
    val context = LocalContext.current

    var tanggalKunjungan by rememberSaveable { mutableStateOf("") }
    var jumlahTiket by rememberSaveable { mutableIntStateOf(1) }
    var tanggalError by remember { mutableStateOf(false) }

    val totalHarga = wisataHargaTiket * jumlahTiket

    // Reset success flag on entry
    LaunchedEffect(Unit) {
        pesananViewModel.resetOrderSuccess()
        pesananViewModel.clearError()
    }

    // Setup date picker
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val formattedMonth = String.format("%02d", month + 1)
            val formattedDay = String.format("%02d", dayOfMonth)
            tanggalKunjungan = "$year-$formattedMonth-$formattedDay"
            tanggalError = false
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    // Prevent picking past dates
    datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pemesanan Tiket",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
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
                    containerColor = ThemeMediumGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(ThemeLightGreen, Color.White)
                    )
                )
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Info Wisata Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Destinasi Pilihan",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ThemeMediumGreen
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = wisataNama,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Place,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Malang Raya",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Harga dasar tiket:",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)
                            )
                            Text(
                                text = formatRupiah(wisataHargaTiket),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ThemeMediumGreen
                                )
                            )
                        }
                    }
                }

                // Booking Form Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Detail Kunjungan",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )

                        // Date field
                        Column {
                            Text(
                                text = "Tanggal Kunjungan",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.DarkGray
                                ),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            OutlinedTextField(
                                value = tanggalKunjungan,
                                onValueChange = {},
                                readOnly = true,
                                placeholder = { Text("Pilih tanggal kunjungan") },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.CalendarMonth,
                                        contentDescription = "Pilih Tanggal",
                                        tint = ThemeMediumGreen
                                    )
                                },
                                isError = tanggalError,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ThemeMediumGreen,
                                    unfocusedBorderColor = Color.LightGray,
                                    errorBorderColor = Color.Red
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { datePickerDialog.show() },
                                enabled = false // visually disabled but clickable Box handles it
                            )
                            // Box overlay to capture click when OutlinedTextField is disabled
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { datePickerDialog.show() }
                            )
                            if (tanggalError) {
                                Text(
                                    text = "Tanggal kunjungan harus diisi",
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                                )
                            }
                        }

                        // Ticket count selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Jumlah Tiket",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.DarkGray
                                    )
                                )
                                Text(
                                    text = "Maksimal 10 tiket per pesanan",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                IconButton(
                                    onClick = { if (jumlahTiket > 1) jumlahTiket-- },
                                    enabled = jumlahTiket > 1,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(
                                            color = if (jumlahTiket > 1) ThemeLightGreen else Color.LightGray.copy(alpha = 0.2f),
                                            shape = CircleShape
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Remove,
                                        contentDescription = "Kurang",
                                        tint = if (jumlahTiket > 1) ThemeMediumGreen else Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Text(
                                    text = jumlahTiket.toString(),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                )

                                IconButton(
                                    onClick = { if (jumlahTiket < 10) jumlahTiket++ },
                                    enabled = jumlahTiket < 10,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(
                                            color = if (jumlahTiket < 10) ThemeLightGreen else Color.LightGray.copy(alpha = 0.2f),
                                            shape = CircleShape
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "Tambah",
                                        tint = if (jumlahTiket < 10) ThemeMediumGreen else Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Total Price Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ThemeLightGreen),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ThemeMediumGreen.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Pembayaran",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = ThemeDarkGreen
                                )
                            )
                            Text(
                                text = "$jumlahTiket Tiket Kunjungan",
                                style = MaterialTheme.typography.bodySmall.copy(color = ThemeMediumGreen)
                            )
                        }
                        Text(
                            text = formatRupiah(totalHarga),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ThemeDarkGreen
                            )
                        )
                    }
                }

                // Error message from VM
                if (uiState.error != null) {
                    Text(
                        text = uiState.error ?: "",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Medium
                    )
                }

                // Submit Button
                Button(
                    onClick = {
                        if (tanggalKunjungan.isBlank()) {
                            tanggalError = true
                        } else {
                            pesananViewModel.createPesanan(
                                wisataId = wisataId,
                                userId = userId,
                                tanggalKunjungan = tanggalKunjungan,
                                jumlahTiket = jumlahTiket,
                                totalHarga = totalHarga,
                                onSuccess = { createdId ->
                                    onPaymentNavigate(createdId, totalHarga)
                                }
                            )
                        }
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ThemeMediumGreen)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Konfirmasi Pemesanan",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
