package com.example.zenmind.uiactivity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bloodtype
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zenmind.BottomBar
import com.example.zenmind.R
import com.example.zenmind.TopApplicationBar
import com.example.zenmind.model.MedicalDatabaseHelper
import com.example.zenmind.model.UserDatabaseHelper
import com.example.zenmind.ui.theme.BabyBlue
import com.example.zenmind.ui.theme.BlushPink
import com.example.zenmind.ui.theme.ButterYellow
import com.example.zenmind.ui.theme.Lavender
import com.example.zenmind.ui.theme.LavenderDark
import com.example.zenmind.ui.theme.TextDark
import com.example.zenmind.ui.theme.TextMedium
import com.example.zenmind.ui.theme.purewhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardPage(
    navController: NavController,
    email: String,
    databaseHelper1: UserDatabaseHelper,
    databaseHelper2: MedicalDatabaseHelper
) {
    Scaffold(
        topBar = { TopApplicationBar("Dashboard", navController) },
        content = { pad -> DashboardContent(pad, databaseHelper1, databaseHelper2, email) },
        bottomBar = { BottomBar(navController) }
    )
}

@Composable
fun DashboardContent(
    h: PaddingValues,
    databaseHelper1: UserDatabaseHelper,
    databaseHelper2: MedicalDatabaseHelper,
    email: String
) {
    val userreg = databaseHelper1.getUserByUseremail(email)
    val usermed = databaseHelper2.medgetUserByUseremail(email)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Lavender)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        // Avatar + greeting card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = purewhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.zen_mind),
                    contentDescription = "Logo",
                    modifier = Modifier.size(72.dp)
                )
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Hello, ${userreg?.name ?: "Friend"} 🌸",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LavenderDark
                    )
                    Text(
                        text = "How are you feeling today?",
                        fontSize = 13.sp,
                        color = TextMedium
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Profile info cards
        if (userreg != null) {
            InfoCard(
                icon = Icons.Outlined.Email,
                label = "Email",
                value = userreg.email ?: "—",
                cardColor = BabyBlue
            )
            Spacer(Modifier.height(10.dp))
            InfoCard(
                icon = Icons.Outlined.Call,
                label = "Mobile",
                value = userreg.mobile ?: "—",
                cardColor = BlushPink
            )
            Spacer(Modifier.height(10.dp))
        }

        // Medical info (if available)
        if (usermed != null) {
            InfoCard(
                icon = Icons.Outlined.Bloodtype,
                label = "Blood Group",
                value = usermed.bloodgrp ?: "—",
                cardColor = ButterYellow
            )
            Spacer(Modifier.height(10.dp))

            // BMI card
            val bmiVal = usermed.bmi?.toFloatOrNull()
            if (bmiVal != null) {
                val (bmiLabel, bmiColor) = when {
                    bmiVal < 18.5f -> "Underweight" to Color(0xFF3F51B5)
                    bmiVal < 25f   -> "Normal ✓" to Color(0xFF028B7F)
                    bmiVal < 30f   -> "Overweight" to Color(0xFFB8891E)
                    bmiVal < 35f   -> "Obese" to Color(0xFFD4711A)
                    else           -> "Extremely Obese" to Color(0xFFB71C1C)
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Lavender),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("BMI", fontSize = 12.sp, color = TextMedium)
                            Text(
                                usermed.bmi.toString(),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(bmiColor.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(bmiLabel, color = bmiColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }

        if (userreg == null) {
            Text("User details not found", fontSize = 16.sp, color = TextMedium)
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun InfoCard(icon: ImageVector, label: String, value: String, cardColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = label, tint = LavenderDark, modifier = Modifier.size(26.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(label, fontSize = 11.sp, color = TextMedium)
                Text(value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
            }
        }
    }
}
