package com.example.studnetrecordmobleapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavController

@Composable
fun registration(navController: NavController, database: Database) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var registrationResult by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Register", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    when {
                        username.isBlank() -> {
                            registrationResult = "Username cannot be empty"
                        }
                        password.isBlank() -> {
                            registrationResult = "Password cannot be empty"
                        }
                        confirmPassword.isBlank() -> {
                            registrationResult = "Confirm Password cannot be empty"
                        }
                        password != confirmPassword -> {
                            registrationResult = "Passwords do not match"
                        }
                        database.isUsernameTaken(username) -> {
                            registrationResult = "Username already taken"
                        }
                        else -> {
                            database.insertUser(username, password)
                            registrationResult = "Registration successful"
                            navController.navigate("login")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Register")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Back to Login")
            }

            registrationResult?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it, color = if (it == "Registration successful") Color.Green else Color.Red)
            }
        }
    }
}
