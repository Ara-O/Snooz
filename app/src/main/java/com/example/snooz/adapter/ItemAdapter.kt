package com.example.snooz.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.snooz.Dream
import com.example.snooz.R
import com.example.snooz.ViewAllDreamsFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import org.w3c.dom.Text

private lateinit var database: DatabaseReference
class ItemAdapter(
    private val dataset: List<Dream>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private lateinit var adapterLayout: View

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
        val textViewTag: TextView = view.findViewById(R.id.item_tag)
        val textViewDream: TextView = view.findViewById(R.id.item_dream)
        val deleteBtn: Button = view.findViewById(R.id.deleteDreamButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.dream_list_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int = dataset.size


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
//        holder.textView.text = context.resources.getString(item.stringResourceId)
        holder.textView.text = item.name
        holder.textViewTag.text = item.tag
        holder.textViewDream.text = item.text
        holder.deleteBtn.setOnClickListener {
            Log.d("dream id clicked ", item.dreamId)
            database = Firebase.database.reference

            val builder = AlertDialog.Builder(adapterLayout.context)
            builder.setTitle("Androidly Alert")
            builder.setMessage("We have a message")
            builder.setPositiveButton("Delete dream") { dialog, which ->
                Toast.makeText(adapterLayout.context,
                    "Delete Dream", Toast.LENGTH_SHORT).show()
                Log.d("efd", "${Firebase.auth.uid}/dreams/${item.dreamId}")
                database.child("${Firebase.auth.uid}/dreams/${item.dreamId}").removeValue()
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                Toast.makeText(adapterLayout.context,
                    "Dream will not be deleted", Toast.LENGTH_SHORT).show()
            }

//            builder.setNeutralButton("Maybe") { dialog, which ->
//                Toast.makeText(adapterLayout.context,
//                    "Maybe", Toast.LENGTH_SHORT).show()
//            }
//            builder.show()


            val alertDialog = builder.create()
            alertDialog.show()
            val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            with(button) {
                setTextColor(Color.RED)
            }
        }
    }
}