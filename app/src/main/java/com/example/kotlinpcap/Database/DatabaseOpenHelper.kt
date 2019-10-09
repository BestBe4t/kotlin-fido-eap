package com.example.kotlinpcap.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseOpenHelper(context: Context, factory: SQLiteDatabase.CursorFactory?):SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object{
        const val Tag = "Database OpenHelper"
        const val DATABASE_NAME = "SoonDea"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "SoonDea"

        val COLUMN_ID = "id"
        val COLUMN_NAME = "username"
        val COLUMN_PASSWORD = "userpassword"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + " INTEAGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + "VARCHAR(30) NOT NULL," +
                COLUMN_PASSWORD + "VARCHAR(30) NOT NULL)"
        db!!.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addUser(username: String, userpassword: String): String{
        var db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE username = " + username, null)
        if(result != null){
            return "UserName is exits!"
        }
        val values = ContentValues()
        values.put(COLUMN_NAME, stringCheck(username))
        values.put(COLUMN_PASSWORD, stringCheck(userpassword))
        db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
        return "Success!"
    }

    fun checkUser(username: String, userpassword: String): Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE username = " + stringCheck(username), null)
        val storePassword = result.getString(2)

        return if(stringCheck(userpassword) == storePassword) true else false
    }

    fun stringCheck(text: String): String{
        do{
            text.replace("\n", "").replace(" ", "").replace("#", "").replace("--", "")
                .replace("-", "").replace("&", "").replace("|", "").replace("*", "")
                .replace(",", "").replace("{", "").replace("}", "").replace("(", "")
                .replace(")", "").replace("\\", "")
        }while (!text.contains("\\"))
        return text
    }
}