package com.example.snooz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.snooz.databinding.ActivitySignupBinding


class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root

        val storedData = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        val sharedPreferencesId = storedData.getString("id", "")


        //Route to main page if user is already signed in
        if(sharedPreferencesId?.length!! != 0){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }

        binding.signupButton.setOnClickListener{
            val i = Intent(this, SignupExtra::class.java).also{
                it.putExtra("fullName", binding.fullNameInput.text.toString())
                it.putExtra("age", binding.ageInput.text.toString())
                it.putExtra("emailAddress", binding.emailInput.text.toString())
                it.putExtra("password", binding.passwordInput.text.toString())
            }
            startActivity(i)
        }

        binding.alreadyHasAccountButton.setOnClickListener {
            val i = Intent(this, Login::class.java)
            startActivity(i)
        }
        setContentView(view)



    }
}