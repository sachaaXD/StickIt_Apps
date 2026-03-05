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

        // 1. Tombol Back
        val btnBack = findViewById<ImageButton>(R.id.btnBackHistory)
        btnBack?.setOnClickListener {
            finish()
        }

        // 2. Setup RecyclerView
        val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)
        rvHistory.layoutManager = LinearLayoutManager(this)

        // 3. LOAD DATA DARI SHAREDPREF (Data yang beneran baru lo beli)
        val dataRiwayat = loadHistoryFromSharedPref()

        // Pasang Adapter ke RecyclerView
        val adapter = HistoryAdapter(dataRiwayat)
        rvHistory.adapter = adapter
    }

    private fun loadHistoryFromSharedPref(): List<HistoryModel> {
        val list = mutableListOf<HistoryModel>()
        val sharedPref = getSharedPreferences("HistoryData", MODE_PRIVATE)
        val count = sharedPref.getInt("HISTORY_COUNT", 0)

        // Baca dari data terbaru (reverse order biar yang baru dibeli paling atas)
        for (i in count downTo 1) {
            val name = sharedPref.getString("H_NAME_$i", null) ?: continue
            val price = sharedPref.getString("H_PRICE_$i", "Rp 0") ?: "Rp 0"
            
            // Tambahin ke list
            list.add(HistoryModel(name, "Success - Payment", "- $price", R.drawable.minion))
        }
        
        // Kalau kosong, kasih data dummy dikit biar gak sepi
        if (list.isEmpty()) {
            list.add(HistoryModel("Belum ada transaksi", "Ayo belanja!", "Rp 0", R.drawable.minion))
        }
        
        return list
    }
}