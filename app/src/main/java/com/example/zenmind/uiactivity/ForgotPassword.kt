package com.example.zenmind.uiactivity

import android.Manifest
import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zenmind.R
import com.example.zenmind.model.UserDatabaseHelper
import com.example.zenmind.ui.theme.BabyBlue
import com.example.zenmind.ui.theme.LavenderDark
import com.example.zenmind.ui.theme.TextDark
import com.example.zenmind.ui.theme.TextMedium
import com.example.zenmind.ui.theme.purewhite
import com.google.firebase.database.DatabaseReference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordPage(
    context: Context,
    navController: NavController,
    databaseReference: DatabaseReference,
    databaseHelper: UserDatabaseHelper
) {
    val get_permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}
    SideEffect { get_permission.launch(Manifest.permission.SEND_SMS) }

    val email = remember { mutableStateOf(TextFieldValue("")) }
    val newpw = remember { mutableStateOf(TextFieldValue("")) }
    val pwvisib = remember { mutableStateOf(false) }
    val otpuser = remember { mutableStateOf(TextFieldValue("")) }
    val otpsys = remember { mutableStateOf("") }

    val fieldColors = TextFieldDefaults.outlinedTextFieldColors(
        containerColor = purewhite,
        focusedBorderColor = LavenderDark,
        unfocusedBorderColor = LavenderDark.copy(alpha = 0.4f),
        focusedLabelColor = LavenderDark,
        cursorColor = LavenderDark
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BabyBlue)
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.zen_mind),
            contentDescription = "Logo",
            modifier = Modifier.size(110.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text("Reset Password", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = LavenderDark)
        Text("We'll send an OTP to your number 💌", fontSize = 13.sp, color = TextMedium)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email.value, onValueChange = { email.value = it }, singleLine = true,
            label = { Text("Email address") }, modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = { Icon(Icons.Outlined.Email, null, tint = LavenderDark) },
            shape = RoundedCornerShape(14.dp), colors = fieldColors
        )
        Spacer(Modifier.height(12.dp))

        val user = databaseHelper.getUserByUseremail(email.value.text)
        Button(
            onClick = {
                if (email.value.text.isNotEmpty()) {
                    otpsys.value = otp()
                    if (user != null) {
                        val sms = SmsManager.getDefault()
                        sms.sendTextMessage("91${user.mobile}", null,
                            "ZenMind OTP: ${otpsys.value}", null, null)
                    }
                    Toast.makeText(context, "OTP sent to registered number", Toast.LENGTH_SHORT).show()
                } else Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(LavenderDark),
            shape = RoundedCornerShape(14.dp)
        ) { Text("Send OTP", color = purewhite, fontWeight = FontWeight.SemiBold) }

        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = otpuser.value, onValueChange = { if (it.text.length <= 6) otpuser.value = it },
            label = { Text("Enter 6-digit OTP") }, modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            leadingIcon = { Icon(Icons.Outlined.Key, null, tint = LavenderDark) },
            shape = RoundedCornerShape(14.dp), colors = fieldColors
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = newpw.value, onValueChange = { newpw.value = it }, singleLine = true,
            label = { Text("New password") }, modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (pwvisib.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { pwvisib.value = !pwvisib.value }) {
                    Icon(if (pwvisib.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, null, tint = LavenderDark)
                }
            },
            shape = RoundedCornerShape(14.dp), colors = fieldColors
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                val usrname = email.value.text.substringBefore("@")
                if (otpsys.value == otpuser.value.text && email.value.text.isNotEmpty() && newpw.value.text.isNotEmpty()) {
                    databaseReference.child(usrname).child("password").setValue(newpw.value.text)
                    databaseHelper.updatePassword(email.value.text, newpw.value.text)
                    Toast.makeText(context, "Password reset! Please sign in 🌸", Toast.LENGTH_SHORT).show()
                    navController.navigate("login")
                } else Toast.makeText(context, "Wrong OTP or missing details", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(LavenderDark),
            shape = RoundedCornerShape(14.dp)
        ) { Text("Reset Password", color = purewhite, fontWeight = FontWeight.SemiBold) }
    }
}

fun otp(): String = (100000..999999).random().toString()
