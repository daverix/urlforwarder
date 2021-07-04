package net.daverix.urlforward.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Brown = Color(0xFF795548)
private val DarkerBrown = Color(0xFF5D4037)
private val Gold = Color(0xFFFFC107)

private val AppLightColors = lightColors(
    primary = Brown,
    primaryVariant = DarkerBrown,
    secondary = Gold,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    background = Color.White
)
private val AppDarkColors = darkColors(
    primary = Brown,
    primaryVariant = DarkerBrown,
    secondary = Gold,
    onPrimary = Color.White,
    background = Color.DarkGray,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun UrlForwarderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) AppDarkColors else AppLightColors,
        content = content
    )
}
