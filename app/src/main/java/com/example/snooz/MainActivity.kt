package com.example.snooz

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.snooz.databinding.ActivityMainBinding
import com.example.snooz.databinding.ActivitySignupExtraBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            val view = binding.root

            setContentView(view)

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