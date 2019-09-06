package com.example.session.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.session.R
import com.example.session.models.Fruit

class FruitAdapter(context: Context, val resourceId: Int, val fruitList: Array<Fruit>): ArrayAdapter<Fruit>(context, resourceId, fruitList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var fruit: Fruit = fruitList.get(position)
        var view:View = convertView ?: LayoutInflater.from(context).inflate(resourceId, parent, false)
//        var view:View = if (convertView == null) {
//             LayoutInflater.from(context).inflate(resourceId, parent, false)
//        } else {
//            convertView
//        }
        var textView:TextView = view.findViewById<TextView>(R.id.fruitName)
        textView.text = fruit.name
        return view
    }
}