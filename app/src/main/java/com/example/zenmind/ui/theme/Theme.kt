package com.example.zenmind.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val PastelColorScheme = lightColorScheme(
    primary = LavenderDark,
    onPrimary = purewhite,
    secondary = BlushPink,
    onSecondary = RoseDark,
    tertiary = BabyBlue,
    onTertiary = SkyDark,
    background = Lavender,
    onBackground = TextDark,
    surface = purewhite,
    onSurface = TextDark,
)

@Composable
fun ZenmindTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = LavenderDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = PastelColorScheme,
        typography = Typography,
        content = content
    )
}
