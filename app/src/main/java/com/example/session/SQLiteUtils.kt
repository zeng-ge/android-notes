package com.example.session

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteUtils(context: Context, dbname: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, dbname, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
//        db?.execSQL("create table Book(id integer primary key autoincrement, name text, price real)")
        db?.execSQL("create table Student(id integer primary key autoincrement, name text, age integer)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}