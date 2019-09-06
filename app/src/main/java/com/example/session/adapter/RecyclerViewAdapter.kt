package com.example.session.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.session.R
import com.example.session.models.Fruit

class  RecyclerViewAdapter constructor(var list: MutableList<Fruit>) : RecyclerView.Adapter<FruitViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): FruitViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fruit_layout, parent, false)
        return FruitViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewHolder: FruitViewHolder, position: Int) {
        val fruit: Fruit = list.get(position)
        viewHolder.nameView.text = fruit.name
    }

}

class FruitViewHolder constructor(var view: View): RecyclerView.ViewHolder(view) {
    var nameView: TextView
    init{
        nameView = view.findViewById(R.id.fruitName)
    }
}