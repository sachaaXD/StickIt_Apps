package com.example.stickit_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val sessionManager = SessionManager(this)

        // Set Nama & Email otomatis dari data yang tadi disimpan di Login
        findViewById<TextView>(R.id.tvProfileName).text = sessionManager.getName()
        findViewById<TextView>(R.id.tvProfileEmail).text = sessionManager.getEmail()

        // Tombol Back
        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        // Tombol Logout
        findViewById<LinearLayout>(R.id.btnLogout).setOnClickListener {
            sessionManager.logout()
            Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}