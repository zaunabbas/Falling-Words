package com.zacoding.android.fallingwords.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zacoding.android.fallingwords.databinding.ActivityMainBinding
import com.zacoding.android.fallingwords.util.makeStatusBarTransparent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
