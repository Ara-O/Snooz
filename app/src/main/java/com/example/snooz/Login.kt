package com.example.snooz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.snooz.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        val TAG = "LOGIN"
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        val email = binding.emailInput
        val password = binding.passwordInput
        auth = Firebase.auth

        Log.d(TAG, email.text.toString())
        Log.d(TAG, password.text.toString())
        binding.loginButton.setOnClickListener {

            //Logging in user with firebase
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser

                        Toast.makeText(baseContext, "You're in!",
                            Toast.LENGTH_SHORT).show()

                        //Route to main page
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)

                        //Store userid in sharedPreferences
                        val sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
                        val firebasePref = sharedPreferences.edit()
                        firebasePref.putString("id", user?.uid.toString())
                        firebasePref.apply()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed: " + task.exception?.message.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }

        setContentView(view)
    }
}