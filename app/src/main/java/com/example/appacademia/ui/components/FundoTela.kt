package com.example.appacademia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.example.appacademia.ui.theme.FundoClaroFim
import com.example.appacademia.ui.theme.FundoClaroInicio
import com.example.appacademia.ui.theme.FundoEscuroFim
import com.example.appacademia.ui.theme.FundoEscuroInicio

@Composable
fun FundoTela(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val darkTheme = isSystemInDarkTheme()
    val gradiente = if (darkTheme) {
        Brush.verticalGradient(listOf(FundoEscuroInicio, FundoEscuroFim))
    } else {
        Brush.verticalGradient(listOf(FundoClaroInicio, FundoClaroFim))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradiente)
    ) {
        content()
    }
}
