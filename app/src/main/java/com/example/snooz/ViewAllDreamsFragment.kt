package com.example.snooz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.snooz.adapter.ItemAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking

class ViewAllDreamsFragment : Fragment() {
    private lateinit var database: DatabaseReference
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var myDataset =  mutableListOf<Dream>()

        //Get data from firebase
        database = Firebase.database.reference


        Log.d("w", "here's the main activity")
        val storedData = activity?.getSharedPreferences("sharedPreferences", AppCompatActivity.MODE_PRIVATE)
        val sharedPreferencesId = storedData?.getString("id", "")


        //If there is a change to the firebase data, update the recyclerview
        runBlocking {
            val databaseDreamsReference = database.child("${sharedPreferencesId}/dreams")
            val dreamsListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    myDataset.clear()
                    for (dream in dataSnapshot.children) {

                        val dreamAdded = Dream(
                            dream.key.toString(),
                            dream.getValue<Dream>()?.name.toString(),
                            dream.getValue<Dream>()?.tag.toString(),
                            dream.getValue<Dream>()?.text.toString(),
                            dream.getValue<Dream>()?.date.toString(),
                        )
                        myDataset.add(dreamAdded)
                    }

                    val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
                    recyclerView?.adapter = ItemAdapter(myDataset)

                    // Use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    recyclerView?.setHasFixedSize(true)
                    // Inflate the layout for this fragment
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting dreams failed, log a message
                    Log.w("aaa", "loadDreams :onCancelled", databaseError.toException())
                }
            }

            databaseDreamsReference.addValueEventListener(dreamsListener)
        }



    }
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_all_dreams, container, false)
    }
}