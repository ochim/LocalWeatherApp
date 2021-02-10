package com.example.localweatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.localweatherapp.databinding.ActivityContributeBinding

class ContributeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContributeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContributeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}