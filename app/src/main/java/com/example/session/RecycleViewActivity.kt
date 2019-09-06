package com.example.session

import android.annotation.SuppressLint
import android.content.*
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.example.session.adapter.RecyclerViewAdapter
import com.example.session.models.Fruit

import kotlinx.android.synthetic.main.activity_recycle_view.*
import kotlinx.android.synthetic.main.content_recycle_view.*
import java.io.*

class RecycleViewActivity : AppCompatActivity() {

    lateinit var receiver: NetworkReceiver

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_view)
        setSupportActionBar(toolbar)

        var fruitList: MutableList<Fruit> = mutableListOf()
        for (index in 0..99) {
            fruitList.add(index, Fruit("fruit $index"))
        }

//        var layoutManager: LinearLayoutManager = LinearLayoutManager(this)
//        layoutManager.orientation = LinearLayoutManager.VERTICAL //垂直滚动
        val layoutManager: GridLayoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = RecyclerViewAdapter(fruitList)

        val intentFilter: IntentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        receiver = NetworkReceiver()
        registerReceiver(receiver, intentFilter)

        val localBoardcastManager: LocalBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBoardcastManager.registerReceiver(receiver, intentFilter)
        toolbar.setOnClickListener(View.OnClickListener {
            sendBroadcast(Intent("com.example.session.Broadcast"))
            localBoardcastManager.sendBroadcast(Intent("android.net.conn.CONNECTIVITY_CHANGE"))
        })

        //读写SharedPreferences
        val shared: SharedPreferences = getSharedPreferences("recycler", Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = shared.edit()
        editor.putInt("age2", 22)
        editor.apply();

        //读写数据库
        var dbUtils = SQLiteUtils(this, "recycler.db", null, 1)
        val values: ContentValues = ContentValues()
        values.put("name", "java")
        dbUtils.writableDatabase.insert("Book", null, values)
        val cursor: Cursor = dbUtils.readableDatabase.query("Book", null, null, null, null, null, null)
        if(cursor.moveToFirst()) {
            val name: String = cursor.getString(cursor.getColumnIndex("name"))
            Log.i("RecycleViewActivity", name)
        }
        cursor.close()

        //写文件
        var out: FileOutputStream = openFileOutput("hello_file.txt", Context.MODE_APPEND)
        var writer: BufferedWriter = BufferedWriter(OutputStreamWriter(out))
        writer.write("hello world1")
        writer.close()

        //读文件
        var reader: BufferedReader = BufferedReader(InputStreamReader(openFileInput("hello_file.txt")))
        Log.i("RecycleViewActivity", reader.readLine())
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}

class NetworkReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("RecycleViewActivity", "network change")
        val connectivityManager: ConnectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo = connectivityManager.activeNetworkInfo
        if(networkInfo?.isAvailable()) {
            Log.i("RecycleViewActivity", "successful" + networkInfo.subtypeName)
        }else {
            Log.i("RecycleViewActivity", "failed" + networkInfo.subtypeName)
        }
    }
}
