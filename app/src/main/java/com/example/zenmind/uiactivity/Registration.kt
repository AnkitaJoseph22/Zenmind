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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zenmind.R
import com.example.zenmind.model.UserDatabaseHelper
import com.example.zenmind.model.UserDetailDb
import com.example.zenmind.model.UserObject
import com.example.zenmind.ui.theme.BlushPink
import com.example.zenmind.ui.theme.Lavender
import com.example.zenmind.ui.theme.LavenderDark
import com.example.zenmind.ui.theme.TextDark
import com.example.zenmind.ui.theme.TextMedium
import com.example.zenmind.ui.theme.purewhite
import com.google.firebase.database.DatabaseReference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationPage(
    context: Context,
    navController: NavController,
    databaseReference: DatabaseReference,
    databaseHelper: UserDatabaseHelper
) {
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val mobile = remember { mutableStateOf(TextFieldValue("")) }
    val regpw = remember { mutableStateOf(TextFieldValue("")) }
    val pwvisib = remember { mutableStateOf(false) }

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
            .background(BlushPink)
            .padding(horizontal = 28.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(40.dp))
        Image(
            painter = painterResource(id = R.drawable.zen_mind),
            contentDescription = "Logo",
            modifier = Modifier.size(110.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text("Create Account", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = LavenderDark)
        Text("Start your zen journey 🌷", fontSize = 14.sp, color = TextMedium)
        Spacer(Modifier.height(26.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            singleLine = true,
            label = { Text("Email address") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = { Icon(Icons.Outlined.Email, null, tint = LavenderDark) },
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            singleLine = true,
            label = { Text("Full name") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.Person, null, tint = LavenderDark) },
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = mobile.value,
            onValueChange = { if (it.text.length <= 10) mobile.value = it },
            singleLine = true,
            label = { Text("Mobile number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            leadingIcon = { Icon(Icons.Outlined.Phone, null, tint = LavenderDark) },
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = regpw.value,
            onValueChange = { regpw.value = it },
            singleLine = true,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (pwvisib.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { pwvisib.value = !pwvisib.value }) {
                    Icon(
                        if (pwvisib.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        null, tint = LavenderDark
                    )
                }
            },
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors
        )
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val usrname = email.value.text.substringBefore("@")
                if (name.value.text.isNotEmpty() && regpw.value.text.isNotEmpty()
                    && mobile.value.text.isNotEmpty() && email.value.text.isNotEmpty()
                ) {
                    // Bug 3 fix: check if email already registered
                    val existingUser = databaseHelper.getUserByUseremail(email.value.text)
                    if (existingUser != null) {
                        Toast.makeText(context, "Email already registered. Please sign in.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    val userObj = UserObject(
                        email.value.text,
                        mobile.value.text,
                        regpw.value.text,
                        name.value.text
                    )
                    databaseReference.child(usrname).setValue(userObj)
                        .addOnFailureListener {
                            Toast.makeText(context, "Registration unsuccessful", Toast.LENGTH_LONG).show()
                        }
                    val user = UserDetailDb(
                        id = null,
                        email = email.value.text,
                        mobile = mobile.value.text,
                        password = regpw.value.text,
                        name = name.value.text,
                    )
                    databaseHelper.insertUser(user)
                    Toast.makeText(context, "Account created! Please sign in 🌸", Toast.LENGTH_SHORT).show()
                    // Go directly to login — no medical registration
                    navController.navigate("login")
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LavenderDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Register", color = purewhite, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(14.dp))
        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Lavender),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Already have an account? Sign in", color = TextDark, fontSize = 14.sp)
        }
        Spacer(Modifier.height(32.dp))
    }
}
