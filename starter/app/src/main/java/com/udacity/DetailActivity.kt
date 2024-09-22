package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancelAll()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.contentDetail.fileName.text = intent.getStringExtra("fileName")
        binding.contentDetail.status.text = intent.getStringExtra("downloadStatus")

        binding.contentDetail.okButton.setOnClickListener {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
            }
        }

    }
}
