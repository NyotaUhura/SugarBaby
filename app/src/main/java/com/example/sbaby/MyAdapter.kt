package com.example.sbaby

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val list: List<String>): RecyclerView.Adapter<MyAdapter.ViewHolder>() {


    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val title: TextView by lazy {
            view.findViewById(R.id.task_title_text_view)
        }

        fun bind(text: String) {
            title.text = text
        }

    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task,null,  false)
       return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = list[position]
        holder.bind(element)
    }
}