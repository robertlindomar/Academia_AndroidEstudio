package com.example.appacademia.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
@Composable
fun BotoesAcaoCard(
    onEditar: () -> Unit,
    onExcluir: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        IconButton(
            onClick = onEditar,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Editar")
        }
        IconButton(
            onClick = onExcluir,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f),
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Excluir")
        }
    }
}
