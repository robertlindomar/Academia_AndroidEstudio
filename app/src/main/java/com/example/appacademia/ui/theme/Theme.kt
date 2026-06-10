package com.example.appacademia.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Color.White,
    primaryContainer = AzulContainer,
    onPrimaryContainer = AzulEscuro,
    secondary = LaranjaAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0B2),
    onSecondaryContainer = Color(0xFF4E2600),
    tertiary = VerdeSucesso,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFC8E6C9),
    onTertiaryContainer = Color(0xFF1B5E20),
    background = FundoClaroInicio,
    onBackground = AzulEscuro,
    surface = Color.White,
    onSurface = AzulEscuro,
    surfaceVariant = Color(0xFFE8EEF4),
    onSurfaceVariant = Color(0xFF546E7A),
    error = VermelhoErro,
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFF7F0000)
)

private val DarkColorScheme = darkColorScheme(
    primary = AzulClaro,
    onPrimary = AzulEscuro,
    primaryContainer = Color(0xFF0D47A1),
    onPrimaryContainer = AzulContainer,
    secondary = LaranjaClaro,
    onSecondary = Color(0xFF4E2600),
    secondaryContainer = Color(0xFFBF360C),
    onSecondaryContainer = Color(0xFFFFE0B2),
    tertiary = Color(0xFF81C784),
    onTertiary = Color(0xFF1B5E20),
    tertiaryContainer = Color(0xFF2E7D32),
    onTertiaryContainer = Color(0xFFC8E6C9),
    background = FundoEscuroInicio,
    onBackground = Color(0xFFE3ECF5),
    surface = Color(0xFF152238),
    onSurface = Color(0xFFE3ECF5),
    surfaceVariant = Color(0xFF1E3050),
    onSurfaceVariant = Color(0xFFB0BEC5),
    error = Color(0xFFEF5350),
    onError = Color.White,
    errorContainer = Color(0xFF7F0000),
    onErrorContainer = Color(0xFFFFCDD2)
)

@Composable
fun AppAcademiaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AcademiaShapes,
        content = content
    )
}
