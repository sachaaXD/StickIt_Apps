package com.example.stickit_app

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class Afterpayment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_afterpayment)

        val sessionManager = SessionManager(this)
        val tvName = findViewById<TextView>(R.id.tvSuccessName)
        val tvPrice = findViewById<TextView>(R.id.tvSuccessPrice)
        val btnFinalPay = findViewById<AppCompatButton>(R.id.btnFinalPay)

        val finalName = intent.getStringExtra("FINAL_NAME") ?: "Sticker"
        val finalPrice = intent.getStringExtra("FINAL_PRICE") ?: "Rp 0"
        val indexesToRemove = intent.getIntegerArrayListExtra("SELECTED_INDEXES")

        tvName.text = finalName
        tvPrice.text = finalPrice

        btnFinalPay.setOnClickListener {
            // 1. Simpan ke History Global (buat Admin) dan History Privat (buat User)
            simpanKeSistemHistory(finalName, finalPrice, sessionManager.getName() ?: "Unknown")

            // 2. Bersihkan keranjang
            hapusItemSetelahBayar(indexesToRemove)

            Toast.makeText(this, "Payment Complete!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Homepage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun simpanKeSistemHistory(stickerName: String, price: String, userName: String) {
        // --- 1. SIMPAN UNTUK ADMIN (Global) ---
        val sharedAdmin = getSharedPreferences("AdminHistory", MODE_PRIVATE)
        val editAdmin = sharedAdmin.edit()
        val countAdmin = sharedAdmin.getInt("COUNT", 0) + 1
        editAdmin.putString("H_NAME_$countAdmin", stickerName)
        editAdmin.putString("H_PRICE_$countAdmin", price)
        editAdmin.putString("H_USER_$countAdmin", userName) // Admin tau siapa yg beli
        editAdmin.putInt("COUNT", countAdmin)
        editAdmin.apply()

        // --- 2. SIMPAN UNTUK USER INI SAJA (Privat) ---
        // Kita pake nama user sebagai nama file SharedPref biar aman
        val sharedUser = getSharedPreferences("History_$userName", MODE_PRIVATE)
        val editUser = sharedUser.edit()
        val countUser = sharedUser.getInt("COUNT", 0) + 1
        editUser.putString("H_NAME_$countUser", stickerName)
        editUser.putString("H_PRICE_$countUser", price)
        editUser.putInt("COUNT", countUser)
        editUser.apply()
    }

    private fun hapusItemSetelahBayar(indexes: ArrayList<Int>?) {
        if (indexes == null) return
        val sharedPref = getSharedPreferences("CartData", MODE_PRIVATE)
        val editor = sharedPref.edit()
        for (index in indexes) {
            editor.remove("ITEM_NAME_$index")
            editor.remove("ITEM_PRICE_$index")
            editor.remove("ITEM_IMAGE_$index")
            editor.remove("ITEM_IMAGE_PATH_$index")
        }
        editor.apply()
    }
}