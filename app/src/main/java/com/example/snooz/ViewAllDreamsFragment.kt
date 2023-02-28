package com.example.snooz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.snooz.adapter.ItemAdapter

class ViewAllDreamsFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myDataset =  listOf("ewww", "webewjwb", "ewinjbwvop", "ewewwwfwefw")
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView?.adapter = ItemAdapter(myDataset)

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView?.setHasFixedSize(true)
        // Inflate the layout for this fragment
    }
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_all_dreams, container, false)
    }
}