package org.sic4change.nut4health.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = ColorScheme(
    primary = ColorPrimary,
    onPrimary = ColorPrimary,
    primaryContainer =  ColorPrimary,
    onPrimaryContainer =  ColorPrimary,
    inversePrimary = ColorPrimary,
    secondary =  ColorPrimaryDark,
    onSecondary =  ColorPrimaryDark,
    secondaryContainer =  ColorPrimaryDark,
    onSecondaryContainer =  ColorPrimaryDark,
    tertiary =  ColorAccent,
    onTertiary =  ColorAccent,
    tertiaryContainer =  ColorAccent,
    onTertiaryContainer =  ColorAccent,
    background =  ColorBackground,
    onBackground =  ColorBackground,
    surface =  ColorSurface,
    onSurface = ColorSurface,
    surfaceVariant = ColorSurface,
    onSurfaceVariant = ColorSurface,
    surfaceTint = ColorSurface,
    inverseSurface =  ColorSurface,
    inverseOnSurface = ColorSurface,
    error = ColorError,
    onError = ColorError,
    errorContainer = ColorError,
    onErrorContainer = ColorError,
    outline = ColorError,
    outlineVariant = ColorError,
    scrim = ColorError)

private val LightColorPalette = ColorScheme(
    primary = ColorPrimary,
    onPrimary = ColorPrimary,
    primaryContainer =  ColorPrimary,
    onPrimaryContainer =  ColorPrimary,
    inversePrimary = ColorPrimary,
    secondary =  ColorPrimaryDark,
    onSecondary =  ColorPrimaryDark,
    secondaryContainer =  ColorPrimaryDark,
    onSecondaryContainer =  ColorPrimaryDark,
    tertiary =  ColorAccent,
    onTertiary =  ColorAccent,
    tertiaryContainer =  ColorAccent,
    onTertiaryContainer =  ColorAccent,
    background =  ColorBackground,
    onBackground =  ColorBackground,
    surface =  ColorSurface,
    onSurface = ColorSurface,
    surfaceVariant = ColorSurface,
    onSurfaceVariant = ColorSurface,
    surfaceTint = ColorSurface,
    inverseSurface =  ColorSurface,
    inverseOnSurface = ColorSurface,
    error = ColorError,
    onError = ColorError,
    errorContainer = ColorError,
    onErrorContainer = ColorError,
    outline = ColorError,
    outlineVariant = ColorError,
    scrim = ColorError)


@Composable
fun NUT4HealthTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    /*val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }*/

    MaterialTheme(
        colorScheme = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}