package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ReconCyan,
    onPrimary = ReconOledBlack,
    primaryContainer = ReconSurfaceDark,
    onPrimaryContainer = ReconCyan,
    secondary = ReconNeonTeal,
    onSecondary = ReconOledBlack,
    secondaryContainer = ReconCardDark,
    onSecondaryContainer = ReconTextPrimary,
    tertiary = ReconAmber,
    onTertiary = ReconOledBlack,
    background = ReconOledBlack,
    onBackground = ReconTextPrimary,
    surface = ReconSurfaceDark,
    onSurface = ReconTextPrimary,
    surfaceVariant = ReconCardDark,
    onSurfaceVariant = ReconTextSecondary,
    outline = ReconCardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
