package com.example.snooz

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.snooz.databinding.FragmentAddDreamBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

//Without id, Dream class is with id tag cause getting id from firebase
class DreamData{
    var name = ""
    var tag = ""
    var text = ""
    var date = ""
    constructor(name: String, tag: String, text: String, date: String){
        this.name = name
        this.tag= tag
        this.text = text
        this.date = date
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

        //Getting the current date in DD/MM/YY format so I can add it to the firebase dreams call
        val dateTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val formattedDateTime = formatter.format(dateTime)

        //Get users current id
        val storedData = activity?.getSharedPreferences("sharedPreferences", AppCompatActivity.MODE_PRIVATE)
        val sharedPreferencesId = storedData?.getString("id", "")

        //once the user clicks the button, push the data to firebase
        _binding!!.addDreamButton.setOnClickListener {

            val currentDreamData = DreamData(binding.dreamNameInput.text.toString(), binding.dreamTagInput.text.toString(), binding.dreamTextInput.text.toString(), formattedDateTime);
            database.child("/${sharedPreferencesId}/dreams").push().setValue(currentDreamData)

            Toast.makeText(context, "Dream added successfully",
                Toast.LENGTH_SHORT).show()
            binding.dreamNameInput.text.clear()
            binding.dreamTagInput.text.clear()
            binding.dreamTextInput.text.clear()
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}