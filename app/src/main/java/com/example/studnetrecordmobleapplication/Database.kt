package com.example.studnetrecordmobleapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "school.db"
        private const val DATABASE_VERSION = 2 // Updated version

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

    // Default data class for students
    data class StudentRec(val id: Int, val name: String, val mark: Int)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_STUDENT_TABLE)

        // Insert admin user
        val adminValues = ContentValues().apply {
            put(COLUMN_USERNAME, "admin")
            put(COLUMN_PASSWORD, "admin")
        }
        db.insert(TABLE_USER, null, adminValues)

        // Insert default student data
        val defaultStudents = listOf(
            StudentRec(1, "Jane Doe", 93),
            StudentRec(2, "John Doe", 92),
            StudentRec(3, "Ahmed Mohamed", 100),
            StudentRec(4, "Homer  Simpson", 29),
            StudentRec(5, "SpongeBob  SquarePants", 95)
        )

        for (student in defaultStudents) {
            val studentValues = ContentValues().apply {
                put(COLUMN_STUDENT_NAME, student.name)
                put(COLUMN_MARK, student.mark)
            }
            db.insert(TABLE_STUDENT, null, studentValues)
        }
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
    fun getAllStudents(): List<StudentRec> {
        val studentList = mutableListOf<StudentRec>()
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
                studentList.add(StudentRec(id, name, mark))
            }
        }
        cursor.close()
        return studentList
    }
    fun getStudentById(studentId: Int): StudentRec? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_STUDENT,
            arrayOf(COLUMN_STUDENT_ID, COLUMN_STUDENT_NAME, COLUMN_MARK),
            "$COLUMN_STUDENT_ID = ?",
            arrayOf(studentId.toString()),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_NAME))
            val mark = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MARK))
            cursor.close()
            StudentRec(id, name, mark)
        } else {
            cursor.close()
            null
        }
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
