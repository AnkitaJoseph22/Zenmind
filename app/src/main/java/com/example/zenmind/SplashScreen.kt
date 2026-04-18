package com.example.zenmind

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zenmind.ui.theme.Lavender
import com.example.zenmind.ui.theme.LavenderDark
import com.example.zenmind.ui.theme.TextMedium
import com.example.zenmind.ui.theme.fillmaxwid
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember { androidx.compose.animation.core.Animatable(0.0f) }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.75f,
            animationSpec = tween(900, easing = {
                OvershootInterpolator(4f).getInterpolation(it)
            })
        )
        delay(800)
        navController.navigate("login")
    }
    Box(
        fillmaxwid
            .fillMaxHeight()
            .background(color = Lavender),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.zen_mind),
                contentDescription = "logo",
                modifier = Modifier
                    .size(180.dp)
                    .scale(scale.value)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "ZenMind",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = LavenderDark,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Your calm, your clarity",
                fontSize = 16.sp,
                color = TextMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
