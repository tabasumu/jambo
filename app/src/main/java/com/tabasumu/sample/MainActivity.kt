package com.tabasumu.sample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.tabasumu.jambo.Jambo
import com.tabasumu.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        Jambo.i("Info : something to do with info")
//        Jambo.v("Verbose : something to do with verbose")
//        Jambo.d("Debug : something to do with debug")
//        Jambo.w("Warn : something to do with warn")
        Jambo.e("Error : something to do with error")
//        Jambo.wtf("Assert : something to do with assert")

        binding.btnMainCrash.setOnClickListener {
            throw RuntimeException("Triggered Crash!")
        }

    }
}