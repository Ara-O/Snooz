package com.example.snooz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.example.snooz.databinding.ActivitySignup2Binding

class Signup2 : AppCompatActivity() {
    private lateinit var binding: ActivitySignup2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignup2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//
//        binding.desiredSleepStartTime.setOnSeekBarChangeListener(object :
//            SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                val value = (progress * (seekBar.width - 2 * seekBar.thumbOffset)) / seekBar.max
//                binding.desiredSleepStartTimeText.text = "" + progress
//                val e = binding.desiredSleepStartTime.thumbOffset / 2.toFloat()
//                binding.desiredSleepStartTimeText.x = seekBar.x + value + e
//                //textView.y = 100f; just added a value set this properly using screen with height aspect ratio, if you do not set it by default it will be there below seek bar
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {
//                // Do something when tracking started
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//                // Do something when tracking stopped
//            }
//        })

    }
}