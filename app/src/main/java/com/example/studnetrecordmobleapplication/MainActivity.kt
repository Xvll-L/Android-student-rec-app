package com.example.studnetrecordmobleapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studnetrecordmobleapplication.ui.theme.StudnetRecordMobleApplicationTheme

class MainActivity : ComponentActivity() {
    private lateinit var database: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Database(this)

        setContent {
            StudnetRecordMobleApplicationTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { loginpage(navController, database) }
                    composable("registration") { registration(navController, database) }
                    composable("studentRecords") { StudentRec(navController, database) }
                    composable("addStudent") { AddStudent(navController, database) }
                    composable("updateStudent/{studentId}") { backStackEntry ->
                        val studentId = backStackEntry.arguments?.getString("studentId")?.toInt() ?: 0
                        updateRec(navController, database, studentId)
                    }
                }
            }
        }
    }
}
