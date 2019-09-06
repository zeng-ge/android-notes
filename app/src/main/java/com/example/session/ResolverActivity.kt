package com.example.session

import android.Manifest
import android.annotation.TargetApi
import android.content.ContentValues
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_resolver.*

class ResolverActivity : AppCompatActivity() {

    var list: MutableList<String> = mutableListOf()

    val requestCode: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resolver)
        listview.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.READ_CONTACTS), requestCode)
        } else {
            readContacts()
        }

        val providerUrl: String = "content://com.example.session.student.provider/Student"
        val uri: Uri = Uri.parse(providerUrl)
        val student: ContentValues = ContentValues()
        student.put("name", "sky")
        contentResolver.insert(uri, student)
        readStudent(uri)
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun readStudent(uri: Uri) {
        val cursor: Cursor? = contentResolver.query(uri, null, null, null)
        if(cursor == null) {
            return;
        }
        if(cursor.moveToFirst()) {
            do{
                val name: String = cursor.getString(cursor.getColumnIndex("name"))
                val age: Int = cursor.getInt(cursor.getColumnIndex("age"))
                Log.i("ResolverActivity", "student name: $name, age: $age")
            }while (cursor.moveToNext())
        }
        cursor.close()
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun readContacts() {
        val cursor: Cursor? = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null)
        if(cursor == null) {
            return;
        }
        if(cursor.moveToFirst()) {
            do{
                val name: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                list.add(name)
            }while (cursor.moveToNext())
        }
        cursor.close()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.get(0) === PackageManager.PERMISSION_GRANTED) {
           readContacts()
        } else {
            Log.i("ResolverActivity", "no permission")
        }
    }
}
