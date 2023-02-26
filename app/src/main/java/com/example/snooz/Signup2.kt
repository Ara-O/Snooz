package com.example.snooz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import com.example.snooz.databinding.ActivitySignup2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Signup2 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignup2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignup2Binding.inflate(layoutInflater)
        val view = binding.root

        val userFullName = intent.getStringExtra("fullName");
        val userAge = intent.getStringExtra("age");
        val email = intent.getStringExtra("emailAddress");
        val password = intent.getStringExtra("password");
        val currentBedtimeHour = binding.currentSleepTimeHour
        val currentBedtimeMinutes = binding.currentSleepTimeMinutes
        val desiredBedtimeHour = binding.desiredSleepTimeHours
        val desiredBedtimeMinutes = binding.desiredSleepTimeMinutes


        //Initializing firebase
        auth = Firebase.auth

        //Checking if user is signed in
        val currentUser = auth.currentUser



        if(currentUser != null){
            Log.d("baaka", "There is a user signed in")
        }else{
            Log.d("baaka", "No user signed in")
        }

        binding.registerButton.setOnClickListener {
            auth.createUserWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser

                        //Storing the current user in sharedpreferences
                        val sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
                        val firebasePref = sharedPreferences.edit()
                        firebasePref.putString("id", user?.uid.toString())
                        firebasePref.apply()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("baaka3", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }
        setContentView(view)


    }
}