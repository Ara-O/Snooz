package com.example.snooz

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initilize authentication
        auth = Firebase.auth
        val currentUser = auth.currentUser


        Log.d("w", "here's the main activity")
        val storedData = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        val sharedPreferencesId = storedData.getString("id", "")
        Log.d("eeeewewfwefw", sharedPreferencesId.toString())

        //Add token listener to check if user signs out
        auth.addIdTokenListener(FirebaseAuth.IdTokenListener {
            if(it.currentUser?.uid == null){
                //Remove user id from shared preferences
                val sharedPreference = getSharedPreferences("sharedPreferences", MODE_PRIVATE).edit()
                sharedPreference.remove("id")
                sharedPreference.apply()

                //Route back to signup page
                val i = Intent(this, Signup::class.java)
                startActivity(i)
            }else{
                Log.d("eee", currentUser.toString())
            }
        })
//
//        findViewById<Button>(R.id.begone).setOnClickListener{
//            val edit = getSharedPreferences("sharedPreferences", MODE_PRIVATE).edit()
//            edit.remove("id")
//            edit.apply()
//            Firebase.auth.signOut()
//
//            Log.d("wewew", auth.currentUser.toString())
//
//        }

   }
}