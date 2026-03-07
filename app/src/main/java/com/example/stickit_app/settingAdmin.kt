package com.example.stickit_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class settingAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_admin)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnLogout = findViewById<TextView>(R.id.btnLogout)
        val tvTotal = findViewById<TextView>(R.id.tvTotalStickers)

        // 1. Tampilkan Statistik Inventory (Hanya info)
        val sharedInv = getSharedPreferences("InventoryData", MODE_PRIVATE)
        val count = sharedInv.getInt("INV_COUNT", 0)
        tvTotal.text = "Total Stickers in Storage: $count"

        btnBack.setOnClickListener { finish() }

        // 2. LOGIKA LOGOUT
        btnLogout.setOnClickListener {
            val sessionManager = SessionManager(this)
            sessionManager.logout()
            
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("LOGOUT", true)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}