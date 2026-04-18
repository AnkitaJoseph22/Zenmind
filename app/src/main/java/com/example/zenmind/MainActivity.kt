package com.example.zenmind

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.RiceBowl
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zenmind.model.MedicalDatabaseHelper
import com.example.zenmind.model.UserDatabaseHelper
import com.example.zenmind.ui.theme.BabyBlue
import com.example.zenmind.ui.theme.BlushPink
import com.example.zenmind.ui.theme.ButterYellow
import com.example.zenmind.ui.theme.Lavender
import com.example.zenmind.ui.theme.LavenderDark
import com.example.zenmind.ui.theme.TextDark
import com.example.zenmind.ui.theme.ZenmindTheme
import com.example.zenmind.ui.theme.fnt18
import com.example.zenmind.ui.theme.fnt24
import com.example.zenmind.ui.theme.horzspacear
import com.example.zenmind.ui.theme.icon
import com.example.zenmind.ui.theme.purewhite
import com.example.zenmind.ui.theme.rcshape
import com.example.zenmind.ui.theme.txtbold
import com.example.zenmind.uiactivity.CaloriePage
import com.example.zenmind.uiactivity.DashboardPage
import com.example.zenmind.uiactivity.EmergencyContactPage
import com.example.zenmind.uiactivity.ForgotPasswordPage
import com.example.zenmind.uiactivity.HealthArticlePage
import com.example.zenmind.uiactivity.LoginPage
import com.example.zenmind.uiactivity.MapPage
import com.example.zenmind.uiactivity.MedicalRegPage
import com.example.zenmind.uiactivity.RegistrationPage
import com.example.zenmind.uiactivity.SleepTrackerPage
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    private lateinit var databaseHelper1: UserDatabaseHelper
    private lateinit var databaseHelper2: MedicalDatabaseHelper

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseHelper1 = UserDatabaseHelper(this)
        databaseHelper2 = MedicalDatabaseHelper(this)
        setContent {
            ZenmindTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val firebasedb = FirebaseDatabase.getInstance()
                    val reference = firebasedb.getReference("User")
                    val get_permission = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestPermission()
                    ) { }
                    SideEffect { get_permission.launch(POST_NOTIFICATIONS) }
                    App(applicationContext, reference, databaseHelper1, databaseHelper2)
                }
            }
        }
    }
}

@Composable
fun App(
    context: Context,
    databaseReference: DatabaseReference,
    databaseHelper1: UserDatabaseHelper,
    databaseHelper2: MedicalDatabaseHelper
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splashscreen") {
        composable("splashscreen") { SplashScreen(navController) }
        composable("reg") { RegistrationPage(context, navController, databaseReference, databaseHelper1) }
        composable("medusreg") { MedicalRegPage(context, navController, databaseHelper2) }
        composable("login") { LoginPage(context, navController, databaseHelper1) }
        composable("forgotpw") { ForgotPasswordPage(context, navController, databaseReference, databaseHelper1) }
        composable("dashboard/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            DashboardPage(navController, email.toString(), databaseHelper1, databaseHelper2)
        }
        composable("caloriemgt") { CaloriePage(navController) }
        composable("article") { HealthArticlePage(navController) }
        composable("maps") { MapPage(navController) }
        composable("sleep") { SleepTrackerPage(navController) }
        composable("emergency") { EmergencyContactPage(navController) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(abc: String) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = LavenderDark),
        title = {
            Text(abc, color = purewhite, fontSize = fnt24, fontWeight = txtbold)
        }
    )
}

@Composable
fun TopApplicationBar(abc: String, navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(LavenderDark)
    ) {
        val openDialog = remember { mutableStateOf(false) }
        val context = LocalContext.current
        Text(
            abc, color = purewhite, fontSize = fnt24, fontWeight = txtbold,
            modifier = Modifier.padding(start = 15.dp)
        )
        IconButton(onClick = { openDialog.value = true }) {
            Icon(Icons.Outlined.Logout, contentDescription = "Logout", tint = purewhite, modifier = icon)
        }
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = { Text("Sign out", fontWeight = FontWeight.Bold, color = TextDark) },
                text = { Text("Are you sure you want to sign out?") },
                containerColor = Lavender,
                confirmButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                            Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()
                            navController.navigate("login")
                        },
                        colors = ButtonDefaults.buttonColors(LavenderDark),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Yes", color = purewhite, fontWeight = FontWeight.SemiBold) }
                },
                dismissButton = {
                    Button(
                        onClick = { openDialog.value = false },
                        colors = ButtonDefaults.buttonColors(BlushPink),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("No", color = TextDark, fontWeight = FontWeight.SemiBold) }
                }
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    // Pastel icon tints per tab
    val tabColors = listOf(ButterYellow, BlushPink, BabyBlue, Lavender, BlushPink)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(LavenderDark),
        horizontalArrangement = horzspacear,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val size22 = Modifier.size(24.dp)
        val butcolor = ButtonDefaults.buttonColors(containerColor = LavenderDark, contentColor = purewhite)

        IconButton(onClick = { navController.navigate("caloriemgt") }) {
            Icon(Icons.Outlined.RiceBowl, "Calorie", tint = ButterYellow, modifier = size22)
        }
        IconButton(onClick = { navController.navigate("article") }) {
            Icon(Icons.Filled.Article, "Article", tint = BlushPink, modifier = size22)
        }
        IconButton(onClick = { navController.navigate("sleep") }) {
            Icon(Icons.Filled.Bedtime, "Sleep", tint = BabyBlue, modifier = size22)
        }
        IconButton(onClick = { navController.navigate("maps") }) {
            Icon(Icons.Filled.LocationOn, "Map", tint = Lavender, modifier = size22)
        }
        IconButton(onClick = { navController.navigate("emergency") }) {
            Icon(Icons.Filled.Contacts, "Emergency", tint = BlushPink, modifier = size22)
        }
    }
}
