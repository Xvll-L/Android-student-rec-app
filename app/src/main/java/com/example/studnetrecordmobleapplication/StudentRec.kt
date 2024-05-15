package com.example.studnetrecordmobleapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun StudentRec(navController: NavController, database: Database) {
    val studentRecords = remember { mutableStateListOf<StudentRecord>() }

    // Retrieve student records from the database in a side effect
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val cursor = database.readableDatabase.query(
                Database.TABLE_STUDENT,
                null,
                null,
                null,
                null,
                null,
                null
            )
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_STUDENT_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_STUDENT_NAME))
                val mark = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_MARK))
                studentRecords.add(StudentRecord(id, name, mark))
            }
            cursor.close()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(studentRecords) { record ->
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(text = record.id.toString(), modifier = Modifier.weight(1f))
                    Text(text = record.name, modifier = Modifier.weight(3f))
                    Text(text = record.mark.toString(), modifier = Modifier.weight(1f))
                }
            }
        }

        Button(
            onClick = { navController.navigate("addStudent") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(text = "Add new student and mark")
        }
    }
}

data class StudentRecord(val id: Int, val name: String, val mark: Int)
