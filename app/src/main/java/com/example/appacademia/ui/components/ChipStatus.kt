package com.example.appacademia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChipStatus(
    texto: String,
    corTexto: Color,
    corFundo: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = texto,
        style = MaterialTheme.typography.labelMedium,
        color = corTexto,
        modifier = modifier
            .background(corFundo, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
