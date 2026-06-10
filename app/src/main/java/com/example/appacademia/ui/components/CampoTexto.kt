package com.example.appacademia.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CampoTexto(
    rotulo: String,
    valor: String,
    onValorChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    mensagemErro: String? = null,
    linhaUnica: Boolean = true
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(rotulo) },
        isError = mensagemErro != null,
        supportingText = if (mensagemErro != null) {
            { Text(mensagemErro) }
        } else null,
        singleLine = linhaUnica,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}
