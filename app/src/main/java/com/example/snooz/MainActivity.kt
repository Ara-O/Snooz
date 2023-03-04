package com.example.snooz

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.snooz.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        database = Firebase.database.reference

        try {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            val view = binding.root

            setContentView(view)

            val fullNameReference = database.child("/${Firebase.auth.uid}/fullName")
            var userFullName:String = ""

            runOnUiThread {
            val fullNameListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    userFullName = dataSnapshot.value.toString()
                    val dateTime = Calendar.getInstance().time
                    val formatter = SimpleDateFormat("EEEE HH:mm a", Locale.ENGLISH)
                    val formattedDateTime = formatter.format(dateTime)

                    binding.welcomeTextView.setText("Welcome ${userFullName}! It is ${formattedDateTime}")

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("ERROR", "Error -", databaseError.toException())
                }
            }

            fullNameReference.addValueEventListener(fullNameListener)
            }



            //Initilize authentication
            auth = Firebase.auth
            val currentUser = auth.currentUser


            Log.d("w", "here's the main activity")
            val storedData = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
            val sharedPreferencesId = storedData.getString("id", "")
            Log.d("eeeewewfwefw", sharedPreferencesId.toString())

            //Add token listener to check if user signs out
            auth.addIdTokenListener(FirebaseAuth.IdTokenListener {
                if (it.currentUser?.uid == null) {
                    //Remove user id from shared preferences
                    val sharedPreference =
                        getSharedPreferences("sharedPreferences", MODE_PRIVATE).edit()
                    sharedPreference.remove("id")
                    sharedPreference.apply()

                    //Route back to signup page
                    val i = Intent(this, Signup::class.java)
                    startActivity(i)
                } else {
                    Log.d("eee", currentUser.toString())
                }
            })
//
            binding.logOutButton.setOnClickListener {
                val edit = getSharedPreferences("sharedPreferences", MODE_PRIVATE).edit()
                edit.remove("id")
                edit.apply()
                Firebase.auth.signOut()

                Log.d("wewew", auth.currentUser.toString())

            }

            binding.addDreamButton.setOnClickListener {
                supportFragmentManager.commit {
                    replace<AddDreamFragment>(R.id.fragmentContainerView)
                    setReorderingAllowed(true)
                    addToBackStack("name") // name can be null
                }
            }

            binding.viewAllDreamsButton.setOnClickListener {
//            val fragmentManager: FragmentManager = supportFragmentManager
//            fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, AddDreamFragment::class.java, null)
//                .setReorderingAllowed(true)
//                .addToBackStack("name").commit()
                supportFragmentManager.commit {
                    replace<ViewAllDreamsFragment>(R.id.fragmentContainerView)
                    setReorderingAllowed(true)
                    addToBackStack("name") // name can be null
                }

            }
        }catch (e: Exception) {
            Log.e("AAAAAAAAA", "onCreateView", e);
            throw e;
        }
   }
}