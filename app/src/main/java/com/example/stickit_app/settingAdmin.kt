package com.example.stickit_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class settingAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_admin)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnLogout = findViewById<TextView>(R.id.btnLogout)
        val btnResetData = findViewById<TextView>(R.id.btnResetData)
        val tvTotal = findViewById<TextView>(R.id.tvTotalStickers)

        val sharedInv = getSharedPreferences("InventoryData", MODE_PRIVATE)
        val count = sharedInv.getInt("INV_COUNT", 0)
        tvTotal.text = "Total Stickers in Storage: $count"

        btnBack.setOnClickListener { finish() }

        // LOGIKA RESET DATA (Kembali ke Default)
        btnResetData.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Reset Inventory?")
                .setMessage("This will delete all custom stickers and edits. Restore to defaults?")
                .setPositiveButton("Reset Now") { _, _ ->
                    val editor = sharedInv.edit()
                    editor.clear() // Hapus semua data inventory
                    editor.apply()
                    
                    Toast.makeText(this, "Inventory Reset! Please restart the app.", Toast.LENGTH_LONG).show()
                    
                    // Keluar ke Login biar data ke-refresh pas buka lagi
                    val sessionManager = SessionManager(this)
                    sessionManager.logout()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("LOGOUT", true)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        // LOGIKA LOGOUT
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