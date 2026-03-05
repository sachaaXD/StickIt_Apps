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

        val tvName = findViewById<TextView>(R.id.tvSuccessName)
        val tvPrice = findViewById<TextView>(R.id.tvSuccessPrice)
        val btnFinalPay = findViewById<AppCompatButton>(R.id.btnFinalPay)

        val finalName = intent.getStringExtra("FINAL_NAME") ?: "Sticker"
        val finalPrice = intent.getStringExtra("FINAL_PRICE") ?: "Rp 0"

        // Tangkap daftar index yang mau dihapus
        val indexesToRemove = intent.getIntegerArrayListExtra("SELECTED_INDEXES")

        tvName.text = finalName
        tvPrice.text = finalPrice

        btnFinalPay.setOnClickListener {
            // 1. Simpan ke History dulu sebelum dihapus dari keranjang
            simpanKeHistory(finalName, finalPrice)

            // 2. PROSES PEMBERSIHAN KERANJANG
            hapusItemSetelahBayar(indexesToRemove)

            Toast.makeText(this, "Payment Complete & History Updated!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Homepage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun simpanKeHistory(name: String, price: String) {
        val sharedPref = getSharedPreferences("HistoryData", MODE_PRIVATE)
        val editor = sharedPref.edit()
        
        val currentCount = sharedPref.getInt("HISTORY_COUNT", 0)
        val newCount = currentCount + 1
        
        // Simpan detail transaksi
        editor.putString("H_NAME_$newCount", name)
        editor.putString("H_PRICE_$newCount", price)
        editor.putInt("HISTORY_COUNT", newCount)
        editor.apply()
    }

    private fun hapusItemSetelahBayar(indexes: ArrayList<Int>?) {
        if (indexes == null) return

        val sharedPref = getSharedPreferences("CartData", MODE_PRIVATE)
        val editor = sharedPref.edit()

        for (index in indexes) {
            editor.remove("ITEM_NAME_$index")
            editor.remove("ITEM_PRICE_$index")
            editor.remove("ITEM_IMAGE_$index")
        }
        editor.apply()
    }
}