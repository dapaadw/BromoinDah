package com.example.bromoindah.ui.screen.review

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    onBackClick: () -> Unit,
    onReviewSuccess: () -> Unit,
    viewModel: ReviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onReviewSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Beri Ulasan") },
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Bagaimana pengalaman Anda?", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            RatingBar(
                rating = uiState.rating,
                onRatingChanged = viewModel::onRatingChanged
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.ulasan,
                onValueChange = viewModel::onUlasanChanged,
                label = { Text("Tulis ulasan Anda (opsional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.error != null) {
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = { viewModel.submitReview() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Kirim Ulasan")
                }
            }
        }
    }
}

@Composable
fun RatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Row {
        repeat(5) { index ->
            val starIndex = index + 1
            IconButton(onClick = { onRatingChanged(starIndex) }) {
                Icon(
                    imageVector = if (starIndex <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = null,
                    tint = if (starIndex <= rating) Color(0xFFFFC107) else Color.Gray
                )
            }
        }
    }
}
