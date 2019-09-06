package com.example.session.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.example.session.SQLiteUtils

class StudentProvider : ContentProvider() {
    lateinit var db: SQLiteUtils;
    override fun onCreate(): Boolean {
        db = SQLiteUtils(context, "student.db", null, 1)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        db.writableDatabase.insert("Student", null, values)
        return uri
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return db.readableDatabase.query("Student", null, null, null, null, null, null)
    }

    override fun getType(uri: Uri): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}