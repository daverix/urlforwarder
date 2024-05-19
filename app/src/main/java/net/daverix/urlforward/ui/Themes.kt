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

private fun appLightColors(backgroundTransparent: Boolean) = lightColors(
    primary = Brown,
    primaryVariant = DarkerBrown,
    secondary = Gold,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    background = if (backgroundTransparent) Color.Transparent else Color.White
)

private fun appDarkColors(backgroundTransparent: Boolean) = darkColors(
    primary = Brown,
    primaryVariant = DarkerBrown,
    secondary = Gold,
    onPrimary = Color.White,
    background = if (backgroundTransparent) Color.Transparent else Color.DarkGray,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun UrlForwarderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    transparentBackground: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme)
            appDarkColors(transparentBackground)
        else
            appLightColors(transparentBackground),
        content = content
    )
}
