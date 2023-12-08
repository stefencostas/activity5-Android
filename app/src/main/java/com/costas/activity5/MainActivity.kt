package com.costas.activity5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.costas.activity5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.managePermissionsButton.setOnClickListener{
            val intent = Intent(this,PermissionHandling::class.java)
            startActivity(intent)
        }
    }
}