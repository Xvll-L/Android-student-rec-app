package com.example.studnetrecordmobleapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "school.db"
        private const val DATABASE_VERSION = 1

        // User Table
        const val TABLE_USER = "User"
        const val COLUMN_USER_ID = "UserID"
        const val COLUMN_USERNAME = "UserName"
        const val COLUMN_PASSWORD = "Password"

        // Student Record Table
        const val TABLE_STUDENT = "Student"
        const val COLUMN_STUDENT_ID = "StudentID"
        const val COLUMN_STUDENT_NAME = "StudentName"
        const val COLUMN_MARK = "Mark"

        // SQL statement to create the user table
        private const val CREATE_USER_TABLE = "CREATE TABLE $TABLE_USER (" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT)"

        // SQL statement to create the student table
        private const val CREATE_STUDENT_TABLE = "CREATE TABLE $TABLE_STUDENT (" +
                "$COLUMN_STUDENT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_STUDENT_NAME TEXT, " +
                "$COLUMN_MARK INTEGER)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_STUDENT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENT")
        onCreate(db)
    }

    // Insert a new student record into the database
    fun insertStudent(studentName: String, mark: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_STUDENT_NAME, studentName)
            put(COLUMN_MARK, mark)
        }
        db.insert(TABLE_STUDENT, null, values)
    }

    // Update an existing student record in the database
    fun updateStudent(studentId: Int, studentName: String, mark: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_STUDENT_NAME, studentName)
            put(COLUMN_MARK, mark)
        }
        db.update(TABLE_STUDENT, values, "$COLUMN_STUDENT_ID = ?", arrayOf(studentId.toString()))
    }

    // Delete a student record from the database
    fun deleteStudent(studentId: Int) {
        val db = writableDatabase
        db.delete(TABLE_STUDENT, "$COLUMN_STUDENT_ID = ?", arrayOf(studentId.toString()))
    }

   // Retrieve all student records from the database
   fun getAllStudents(): List<StudentRecord> {
       val studentList = mutableListOf<StudentRecord>()
       val db = readableDatabase
       val cursor = db.query(
           TABLE_STUDENT,
           arrayOf(COLUMN_STUDENT_ID, COLUMN_STUDENT_NAME, COLUMN_MARK),
           null,
           null,
           null,
           null,
           null
       )
       with(cursor) {
           while (moveToNext()) {
               val id = getInt(getColumnIndexOrThrow(COLUMN_STUDENT_ID))
               val name = getString(getColumnIndexOrThrow(COLUMN_STUDENT_NAME))
               val mark = getInt(getColumnIndexOrThrow(COLUMN_MARK))
               studentList.add(StudentRecord(id, name, mark))
           }
       }
       cursor.close()
       return studentList
   }

    // Insert a new user account into the database
    fun insertUser(username: String, password: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        db.insert(TABLE_USER, null, values)
    }

    // Check if a user with the given username exists in the database
    fun isUsernameTaken(username: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USER,
            arrayOf(COLUMN_USER_ID),
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null
        )
        val isTaken = cursor.count > 0
        cursor.close()
        return isTaken
    }

    // Authenticate user login
    fun authenticateUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USER,
            arrayOf(COLUMN_USER_ID),
            "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password),
            null,
            null,
            null
        )
        val isAuthenticated = cursor.count > 0
        cursor.close()
        return isAuthenticated
    }
}

data class StudentRec(val id: Int, val name: String, val mark: Int)