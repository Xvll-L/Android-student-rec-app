package com.example.studnetrecordmobleapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@Composable
fun StudentRec(navController: NavController, database: Database) {
    val studentRecords = remember { mutableStateListOf<StudentRecord>() }
    val searchQuery = remember { mutableStateOf("") }
    val searchResults = remember { mutableStateListOf<StudentRecord>() }
    val coroutineScope = rememberCoroutineScope()

    // Retrieve student records from the database in a side effect
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            refreshStudentRecords(database, studentRecords)
            searchResults.addAll(studentRecords)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(searchResults) { record ->
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(text = record.id.toString(), modifier = Modifier.weight(1f))
                    Text(text = record.name, modifier = Modifier.weight(3f))
                    Text(text = record.mark.toString(), modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(id = R.drawable.update_24dp_fill0_wght400_grad0_opsz24),
                        contentDescription = "Update",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                navController.navigate("updateStudent/${record.id}")
                            }
                    )

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                database.deleteStudent(record.id)
                                refreshStudentRecords(database, studentRecords)
                                searchResults.clear()
                                searchResults.addAll(studentRecords)
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(text = "Delete")
                    }
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

    LaunchedEffect(searchQuery.value) {
        searchResults.clear()
        if (searchQuery.value.isBlank()) {
            searchResults.addAll(studentRecords)
        } else {
            val query = searchQuery.value.toLowerCase(Locale.getDefault())
            searchResults.addAll(
                studentRecords.filter { record ->
                    record.name.toLowerCase(Locale.getDefault()).contains(query) ||
                            record.id.toString().contains(query) ||
                            record.mark.toString().contains(query)
                }
            )
        }
    }
}

suspend fun refreshStudentRecords(database: Database, studentRecords: MutableList<StudentRecord>) {
    withContext(Dispatchers.IO) {
        studentRecords.clear()
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

data class StudentRecord(val id: Int, val name: String, val mark: Int)
