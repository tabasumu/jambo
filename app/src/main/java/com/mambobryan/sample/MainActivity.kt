package com.mambobryan.sample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.mambobryan.jambo.Jambo
import com.mambobryan.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

//        Jambo.i("Info : something to do with info")
//        Jambo.e("Error : something to do with error")
//        Jambo.v("Verbose : something to do with verbose")
//        Jambo.d("Debug : something to do with debug")
//        Jambo.w("Warn : something to do with warn")
//        Jambo.wtf("Assert : something to do with assert")
//
//        throw RuntimeException("Triggered crash");

    }
}