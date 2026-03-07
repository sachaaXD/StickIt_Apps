package com.example.stickit_app

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val btnBack = findViewById<ImageButton>(R.id.btnBackHistory)
        btnBack?.setOnClickListener { finish() }

        val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)
        rvHistory.layoutManager = LinearLayoutManager(this)

        // LOAD DATA SEMUA USER (Buat Admin)
        val allHistory = loadAdminHistory()
        rvHistory.adapter = HistoryAdapter(allHistory)
    }

    private fun loadAdminHistory(): List<HistoryModel> {
        val list = mutableListOf<HistoryModel>()
        val sharedPref = getSharedPreferences("AdminHistory", MODE_PRIVATE)
        val count = sharedPref.getInt("COUNT", 0)

        // Baca data dari yang terbaru
        for (i in count downTo 1) {
            val name = sharedPref.getString("H_NAME_$i", "Sticker") ?: "Sticker"
            val price = sharedPref.getString("H_PRICE_$i", "Rp 0") ?: "Rp 0"
            val user = sharedPref.getString("H_USER_$i", "Unknown User") ?: "Unknown User"
            
            // TAMPILKAN NAMA PEMBELI SEBAGAI SUBTITLE
            list.add(HistoryModel(name, "Buyer: $user", "- $price", R.drawable.minion))
        }
        
        if (list.isEmpty()) {
            list.add(HistoryModel("No Transactions", "Waiting for users to shop", "Rp 0", R.drawable.minion))
        }
        return list
    }
}