package com.example.snooz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.snooz.databinding.FragmentAddDreamBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Dream{
    var name = ""
    var tag = ""
    var text = ""
    constructor(name: String, tag: String, text: String){
        this.name = name
        this.tag= tag
        this.text = text
    }
}

class AddDreamFragment : Fragment() {
    private var _binding: FragmentAddDreamBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddDreamBinding.inflate(inflater, container, false)
        val view = binding.root

        database = Firebase.database.reference

        //Get users current id
        val storedData = activity?.getSharedPreferences("sharedPreferences", AppCompatActivity.MODE_PRIVATE)
        val sharedPreferencesId = storedData?.getString("id", "")

        //once the user clicks the button, push the data to firebase
        _binding!!.addDreamButton.setOnClickListener {
            val currentDreamData = Dream(binding.dreamNameInput.text.toString(), binding.dreamTagInput.text.toString(), binding.dreamTextInput.text.toString());
            database.child("/${sharedPreferencesId}/dreams").push().setValue(currentDreamData)
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}