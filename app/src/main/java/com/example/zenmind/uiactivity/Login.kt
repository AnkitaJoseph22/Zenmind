package com.example.zenmind.uiactivity

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zenmind.R
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
fun LoginPage(context: Context, navController: NavController, databaseHelper: UserDatabaseHelper) {
    val mail = remember { mutableStateOf(TextFieldValue("")) }
    val pw = remember { mutableStateOf(TextFieldValue("")) }
    val pwvisib = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Lavender)
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.zen_mind),
            contentDescription = "Logo",
            modifier = Modifier.size(130.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "ZenMind", fontSize = 28.sp, fontWeight = FontWeight.Bold,
            color = LavenderDark
        )
        Text(
            "Welcome back 🌸", fontSize = 15.sp, color = TextMedium
        )
        Spacer(Modifier.height(28.dp))

        OutlinedTextField(
            value = mail.value,
            onValueChange = { mail.value = it },
            singleLine = true,
            label = { Text("Email address") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = LavenderDark) },
            shape = RoundedCornerShape(14.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = purewhite,
                focusedBorderColor = LavenderDark,
                unfocusedBorderColor = LavenderDark.copy(alpha = 0.4f),
                focusedLabelColor = LavenderDark,
                cursorColor = LavenderDark
            )
        )
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            value = pw.value,
            onValueChange = { pw.value = it },
            singleLine = true,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (pwvisib.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { pwvisib.value = !pwvisib.value }) {
                    Icon(
                        imageVector = if (pwvisib.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null, tint = LavenderDark
                    )
                }
            },
            shape = RoundedCornerShape(14.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = purewhite,
                focusedBorderColor = LavenderDark,
                unfocusedBorderColor = LavenderDark.copy(alpha = 0.4f),
                focusedLabelColor = LavenderDark,
                cursorColor = LavenderDark
            )
        )
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate("forgotpw") }) {
            Text("Forgot password?", color = LavenderDark, fontSize = 14.sp)
        }
        Spacer(Modifier.height(10.dp))

        Button(
            onClick = {
                if (mail.value.text.isNotEmpty() && pw.value.text.isNotEmpty()) {
                    val user = databaseHelper.getUserByUseremail(mail.value.text)
                    if (user != null && user.password == pw.value.text) {
                        navController.navigate("dashboard/${mail.value.text}")
                        Toast.makeText(context, "Welcome back! 🌸", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LavenderDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Sign In", color = purewhite, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(20.dp))

        // Soft divider row
        Text("— or —", color = TextMedium, fontSize = 13.sp)
        Spacer(Modifier.height(14.dp))

        Button(
            onClick = { navController.navigate("reg") },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlushPink),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Create an Account", color = TextDark, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
