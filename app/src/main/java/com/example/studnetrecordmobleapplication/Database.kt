package com.example.studnetrecordmobleapplication

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
}
