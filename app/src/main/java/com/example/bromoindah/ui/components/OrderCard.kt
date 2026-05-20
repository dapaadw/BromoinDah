package com.example.bromoindah.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bromoindah.data.model.Pesanan

private val ThemeDarkGreen = Color(0xFF1B5E20)
private val ThemeMediumGreen = Color(0xFF2E7D32)
private val ThemeLightGreen = Color(0xFFE8F5E9)

@Composable
fun OrderCard(
    pesanan: Pesanan,
    onPayClick: (pesananId: String, totalHarga: Int) -> Unit,
    onReviewClick: (pesananId: String, wisataId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Map status code
    val isPending = pesanan.status.equals("pending", ignoreCase = true)
    val isApproved = pesanan.status.equals("approved", ignoreCase = true) || pesanan.status.equals("success", ignoreCase = true)
    val isRejected = pesanan.status.equals("rejected", ignoreCase = true) || pesanan.status.equals("failed", ignoreCase = true)
    val isWaitingVerification = isPending && !pesanan.buktiBayarUrl.isNullOrBlank()

    val badgeColor: Color
    val badgeBg: Color
    val statusText: String
    val badgeIcon: androidx.compose.ui.graphics.vector.ImageVector

    when {
        isWaitingVerification -> {
            badgeColor = Color(0xFF0288D1) // Blue
            badgeBg = Color(0xFFE1F5FE)
            statusText = "Menunggu Verifikasi"
            badgeIcon = Icons.Filled.HourglassEmpty
        }
        isPending -> {
            badgeColor = Color(0xFFF57C00) // Orange
            badgeBg = Color(0xFFFFF3E0)
            statusText = "Menunggu Pembayaran"
            badgeIcon = Icons.Filled.Paid
        }
        isApproved -> {
            badgeColor = ThemeMediumGreen // Green
            badgeBg = ThemeLightGreen
            statusText = "Dikonfirmasi"
            badgeIcon = Icons.Filled.Verified
        }
        else -> {
            badgeColor = Color(0xFFD32F2F) // Red
            badgeBg = Color(0xFFFFEBEE)
            statusText = "Dibatalkan"
            badgeIcon = Icons.Filled.ErrorOutline
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Code and Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.ConfirmationNumber,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "PESAN-${pesanan.id?.takeLast(6)?.uppercase() ?: "CODE"}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    )
                }

                // Badge Container
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = badgeIcon,
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = badgeColor
                        )
                    )
                }
            }

            // Body: Destination details
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Info Column
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pesanan.wisata?.nama ?: "Destinasi Wisata",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = pesanan.tanggalKunjungan,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            imageVector = Icons.Filled.LocalActivity,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${pesanan.jumlahTiket} Tiket",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
                        )
                    }
                }
            }

            // Divider-like Row for Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Pembayaran",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
                Text(
                    text = formatRupiah(pesanan.totalHarga),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ThemeMediumGreen
                    )
                )
            }

            // Action Buttons
            if (isPending && pesanan.buktiBayarUrl.isNullOrBlank()) {
                Button(
                    onClick = { onPayClick(pesanan.id ?: "", pesanan.totalHarga) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ThemeMediumGreen)
                ) {
                    Text(
                        text = "Bayar Sekarang",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            } else if (isApproved) {
                OutlinedButton(
                    onClick = { onReviewClick(pesanan.id ?: "", pesanan.wisataId) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ThemeMediumGreen),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(ThemeMediumGreen))
                ) {
                    Icon(
                        imageVector = Icons.Filled.RateReview,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Beri Ulasan",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
