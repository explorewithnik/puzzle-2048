package com.niketchoudhary.`in`.puzzle_2048_codding_challenge.owner.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.niketchoudhary.`in`.puzzle_2048_codding_challenge.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    lateinit var binding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}