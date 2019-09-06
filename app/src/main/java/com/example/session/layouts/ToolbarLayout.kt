package com.example.session.layouts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.session.R
import kotlinx.android.synthetic.main.activity_toolbar_layout.*
import java.io.*

class ToolbarLayout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_layout)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setCheckedItem(R.id.java)
        navigationView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        floatActionButtion.setOnClickListener {
            //从it这个view向上找最外层的布局
            Snackbar
                .make(it, "on click float action button", Snackbar.LENGTH_SHORT)
                .setAction("hello", {
                    Toast.makeText(this@ToolbarLayout, "hello world", Toast.LENGTH_SHORT).show()
                })
                .show()
        }

        Glide.with(this).load("https://artwork-assets-staging-sbux.starbucks.com.cn/mktcontent/9e10cbd8-310b-4f4d-b924-c3de1e1e253e_5e131a0a/x1.jpeg").into(findViewById(R.id.headerImage))
//        Glide.with(this).load("https://artwork-assets-staging-sbux.starbucks.com.cn/mktcontent/9e10cbd8-310b-4f4d-b924-c3de1e1e253e_5e131a0a/x1.jpeg").into(findViewById(R.id.glideImage))


//        swipeRefreshLayout.setOnRefreshListener {
//            Thread(Runnable {
//                Thread.sleep(2000)
//                runOnUiThread(Runnable {
//                    swipeRefreshLayout.isRefreshing = false //关闭刷新
//                })
//            }).start()
//        }

        // 内部存储app文件目录， 内部是相对于APP本身的 /data/user/0/com.example.session/files
        Log.i("ToolbarLayout", "getFileDir:   $filesDir")

        // 内部存储app缓存目录 /data/user/0/com.example.session/cache
        Log.i("ToolbarLayout", "getCachedDir: $cacheDir")

        // 外部目录， 仅当前APP能访问 /storage/emulated/0/Android/data/com.example.session/files
        Log.i("ToolbarLayout", "getExternalFilesDir: ${getExternalFilesDir(null).absolutePath}")
        val file: File = File(getExternalFilesDir(""), "toolbar.txt");
        BufferedWriter(OutputStreamWriter(FileOutputStream(file))).also {
            it.write("hello wold:toolbar")
            it.close()
        }

        // 外部目录 /storage/emulated/0/Android/data/com.example.session/cache
        Log.i("ToolbarLayout", "getExternalCachedDir: $externalCacheDir")

        //公共目录，所有APP都可以访问，如图片、视频、相册等 /storage/emulated/0
        Log.i("ToolbarLayout", "getExternalStorageDirectory: ${Environment.getExternalStorageDirectory()}")

        //获取外部目录，最好先判断一下storage有没有mounted,
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.i("ToolbarLayout", "getExternalStorageState: mounted")
        }

        //特定公共目录，如下载目录/storage/emulated/0/Download
        Log.i("ToolbarLayout", "getExternalStorageDirectory: ${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}")
        /***
         *  getFileDir:   /data/user/0/com.example.session/files            不需要申请权限
            getCachedDir: /data/user/0/com.example.session/cache            不需要申请权限
            getExternalFilesDir: /storage/emulated/0/Android/data/com.example.session/files     不需要申请权限
            getExternalCachedDir: /storage/emulated/0/Android/data/com.example.session/cache    不需要申请权限
            getExternalStorageDirectory: /storage/emulated/0                读写文件，要申请权限，android 6.0以上还要动态申请权限，单纯获取路径不需要申请权限
            getExternalStorageDirectory: /storage/emulated/0/Download       读写文件，要申请权限，android 6.0以上还要动态申请权限，单纯获取路径不需要申请权限
         */
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            } else {
                writeFileToDownloadFolder();
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(grantResults.get(0) === PackageManager.PERMISSION_GRANTED) {
            writeFileToDownloadFolder();
        }
    }

    fun writeFileToDownloadFolder() {
        var file2: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "download.txt")
        BufferedWriter(OutputStreamWriter(FileOutputStream(file2))).also {
            it.write("hello wold:download")
            it.close()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item!!.itemId
        when(itemId) {
            R.id.add -> Toast.makeText(this, "add item", Toast.LENGTH_SHORT).show()
            R.id.update -> Toast.makeText(this, "update item", Toast.LENGTH_SHORT).show()
            R.id.delete -> Toast.makeText(this, "delete item", Toast.LENGTH_SHORT).show()
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return true
    }
}
