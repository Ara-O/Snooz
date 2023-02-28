package com.example.snooz.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.snooz.R
import com.example.snooz.ViewAllDreamsFragment

class ItemAdapter(
    private val dataset: List<String>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.dream_list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int = dataset.size


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
//        holder.textView.text = context.resources.getString(item.stringResourceId)
        holder.textView.text = item
    }
}