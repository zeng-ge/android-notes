package com.example.session

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.Toast
import com.example.session.adapter.FruitAdapter
import com.example.session.models.Fruit
import kotlinx.android.synthetic.main.activity_customize_component.*
import kotlinx.android.synthetic.main.content_customize_component.*

class CustomizeComponentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customize_component)
        setSupportActionBar(toolbar)
        var fruitList:Array<Fruit> = arrayOf(Fruit("apple"), Fruit("lemon"))
        val fruitAdapter = FruitAdapter(this, R.layout.fruit_layout, fruitList)
        listView.adapter = fruitAdapter
        listView.setOnItemClickListener { parent, view, position, id ->
            var fruit:Fruit = fruitList[position]
            Toast.makeText(this, fruit.name, Toast.LENGTH_SHORT).show()
        }
    }

}
