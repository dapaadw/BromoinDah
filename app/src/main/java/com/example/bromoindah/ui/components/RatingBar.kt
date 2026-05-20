package com.example.bromoindah.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val StarColor = Color(0xFFFFB300)
private val StarUnfilledColor = Color(0xFFE0E0E0)

@Composable
fun RatingBar(
    rating: Int,
    onRatingChange: ((Int) -> Unit)? = null,
    size: Dp = 24.dp,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..5) {
            val isFilled = i <= rating
            val starModifier = if (onRatingChange != null) {
                Modifier
                    .size(size)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onRatingChange(i)
                    }
            } else {
                Modifier.size(size)
            }

            Icon(
                imageVector = if (isFilled) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = if (isFilled) "Bintang $i terisi" else "Bintang $i kosong",
                modifier = starModifier,
                tint = if (isFilled) StarColor else StarUnfilledColor
            )
        }
    }
}
