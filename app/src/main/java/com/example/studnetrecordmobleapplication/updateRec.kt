package com.example.studnetrecordmobleapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavBackStackEntry

@Composable
fun updateRec(navController: NavController, database: Database, studentId: Int) {
    var studentName by remember { mutableStateOf("") }
    var studentMark by remember { mutableStateOf("") }

    // Load the student record from the database
    LaunchedEffect(studentId) {
        val student = database.getStudentById(studentId)
        studentName = student?.name ?: ""
        studentMark = student?.mark?.toString() ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Update Student Record")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = studentName,
            onValueChange = { studentName = it },
            label = { Text("Student Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = studentMark,
            onValueChange = { studentMark = it },
            label = { Text("Student Mark") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val mark = studentMark.toIntOrNull()
                if (studentName.isNotBlank() && mark != null) {
                    // Update the student record in the database
                    database.updateStudent(studentId, studentName, mark)
                    // Navigate back to the student record screen
                    navController.navigateUp()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                navController.navigateUp()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cancel")
        }
    }
}
