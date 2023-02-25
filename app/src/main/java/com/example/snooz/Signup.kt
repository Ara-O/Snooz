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

        binding.signupButton.setOnClickListener{
            val i = Intent(this, Signup2::class.java)
            startActivity(i)
        }
        setContentView(view)



    }
}