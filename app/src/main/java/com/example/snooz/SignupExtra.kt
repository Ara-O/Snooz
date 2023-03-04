package com.example.snooz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.snooz.databinding.ActivitySignupExtraBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupExtra : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignupExtraBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupExtraBinding.inflate(layoutInflater)
        val view = binding.root

        val userFullName = intent.getStringExtra("fullName");
        val userAge = intent.getStringExtra("age");
        val email = intent.getStringExtra("emailAddress");
        val password = intent.getStringExtra("password");
        val currentBedtimeHour = binding.currentSleepTimeHour.text
        val currentBedtimeMinutes = binding.currentSleepTimeMinutes.text
        val desiredBedtimeHour = binding.desiredSleepTimeHours.text
        val desiredBedtimeMinutes = binding.desiredSleepTimeMinutes.text


        //Initializing firebase
        auth = Firebase.auth

        binding.registerButton.setOnClickListener {
            auth.createUserWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser

                        //Storing the current user in shared preferences
                        val sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
                        val firebasePref = sharedPreferences.edit()
                        firebasePref.putString("id", user?.uid.toString())
                        firebasePref.apply()

                        val userId = user?.uid.toString()


                        //Store data of preferred sleep time in firebase
                        val database = Firebase.database.reference
                        database.child(userId).child("fullName").setValue("$userFullName")
                        database.child(userId).child("userAge").setValue("$userAge")
                        database.child(userId).child("email").setValue("$email")
                        database.child(userId).child("currentBedTime").setValue("$currentBedtimeHour:$currentBedtimeMinutes")
                        database.child(userId).child("desiredBedTime").setValue("$desiredBedtimeHour:$desiredBedtimeMinutes")



                        //Move to the next page
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("signupExtra", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed: " + task.exception?.message.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }
        setContentView(view)


    }
}