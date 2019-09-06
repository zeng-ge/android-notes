package com.example.session.layouts

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.renderscript.Long3
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import com.example.session.R
import kotlinx.android.synthetic.main.activity_take_photo.*
import java.io.File
import java.net.URI
import java.nio.file.Files

class TakePhoto : AppCompatActivity() {

    lateinit var imageUrl: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)


        takePhoto.setOnClickListener {
            takePhoto()
        }

        choosePhoto.setOnClickListener {
            selectPhoto()
        }
        getAllImageList()
    }

    fun getAllImageList() {
        val imageList: MutableList<String> = mutableListOf()
        val cursor: Cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
        if(cursor.moveToFirst()) {
            do{
                val imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                imageList.add(imagePath)
                Log.i("TakePhoto", imagePath)
            } while (cursor.moveToNext())
        }
    }

    fun requestPermission(code: Int): Boolean {
        val permissions: MutableList<String> = mutableListOf()
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA)
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if(permissions.size > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions.toTypedArray(), code)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(grantResults.all { it === PackageManager.PERMISSION_GRANTED } ) {
            when(requestCode) {
                1 -> takePhoto()
                2 -> selectPhoto()
            }
        }
    }

    fun takePhoto() {
        if(!requestPermission(1)){
            return
        }
        val imageFile: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "temp.jpg")
        imageFile.exists().also {
            imageFile.delete()
        }
        imageFile.createNewFile()
        Log.i("TakePhoto", imageFile.absolutePath)
        //android 7开始，不能直接使用文件地址，要用FileProvider包装
        imageUrl = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(this, "com.example.session.file.provider", imageFile)
        } else {
            Uri.fromFile(imageFile)
        }

        val intent: Intent = Intent("android.media.action.IMAGE_CAPTURE")//拍照的action, MediaStore.ACTION_IMAGE_CAPTURE
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl)
        startActivityForResult(intent, 10)
    }

    fun selectPhoto() {
        if(!requestPermission(2)){
            return
        }
        val intent:Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if(resultCode !== Activity.RESULT_OK) {
            return
        }
        when(requestCode) {
            10 -> {
                val image: Bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUrl))
                imageView.setImageBitmap(image)
            }
            100 -> {
                intent?.data.also{
                    val uri: Uri = it!!
                    var filePath: String? = null
                    /**
                     * 从android 4.4即API19开始，返回的不是直实的uri，需要特殊处理
                     * content://com.android.providers.media.documents/document/image%3A11
                     * 相当于：
                     * content://com.android.providers.media.documents/document/image:11
                     *
                     * MediaStore.Images.Media.EXTERNAL_CONTENT_URI相当于content://media/external/images/media
                     * 最终解析出来的结果：
                     *   /storage/emulated/0/DCIM/Camera/IMG_20190701_022015.jpg
                     * */
                    if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if(DocumentsContract.isDocumentUri(this, uri)){
                            val docId: String = DocumentsContract.getDocumentId(uri)
                            if("com.android.providers.media.documents".equals(uri.authority)){
                                val id: String = docId.split(":")[1]//得到11
                                val selection: String = "${MediaStore.Images.Media._ID}=$id"
                                filePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
                            } else if("com.android.providers.downloads.docuemnts".equals(uri.authority)) {
                                filePath = getImagePath(
                                    ContentUris.withAppendedId(
                                        Uri.parse("content://downloads/public_downloads"),
                                        docId.toLong()
                                    )
                                )
                            }
                        } else if("content".equals(uri.scheme.toLowerCase())) {
                            filePath = getImagePath(uri)
                        } else if("files".equals(uri.scheme.toLowerCase())) {
                            filePath = uri.path
                        }
                    } else {
                        filePath = getImagePath(uri)
                    }
                    filePath?.let{
                        imageView.setImageBitmap(BitmapFactory.decodeFile(filePath))
                    }

                    Log.i("TakePhoto", intent?.data.toString())
                }
            }
        }
    }

    fun getImagePath(uri: Uri, selection:String? = null): String? {
        val cursor: Cursor = contentResolver.query(uri, null, selection, null, null)

        var filePath = cursor?.moveToFirst().let {
            val path:String = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            path
        }
        return filePath
    }
}
