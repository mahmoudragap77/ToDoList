package com.training.todolist.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ToDoListDataBase(context: Context) :
    SQLiteOpenHelper(context, ConsDataBase.DATABASE_NAME, null, ConsDataBase.DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE " + ConsDataBase.TABLE_NAME + " (" +
                ConsDataBase.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ConsDataBase.TITLE + " TEXT, " +
                ConsDataBase.DESCRIPTION + " TEXT, " +
                ConsDataBase.COMPLETED + " INTEGER DEFAULT 0)"

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE ${ConsDataBase.TABLE_NAME} ADD COLUMN ${ConsDataBase.COMPLETED} INTEGER DEFAULT 0")
        }
    }
}