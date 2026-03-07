package com.example.stickit_app

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class historyUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_user)

        val btnBack = findViewById<ImageButton>(R.id.btnBackHistory)
        btnBack?.setOnClickListener { finish() }

        val rvHistory = findViewById<RecyclerView>(R.id.rvHistoryUser)
        rvHistory.layoutManager = LinearLayoutManager(this)

        // LOAD DATA KHUSUS USER INI
        val dataRiwayat = loadMyHistory()
        rvHistory.adapter = HistoryAdapter(dataRiwayat)
    }

    private fun loadMyHistory(): List<HistoryModel> {
        val sessionManager = SessionManager(this)
        val userName = sessionManager.getName() ?: "Unknown"
        
        val list = mutableListOf<HistoryModel>()
        val sharedPref = getSharedPreferences("History_$userName", MODE_PRIVATE)
        val count = sharedPref.getInt("COUNT", 0)

        for (i in count downTo 1) {
            val name = sharedPref.getString("H_NAME_$i", null) ?: continue
            val price = sharedPref.getString("H_PRICE_$i", "Rp 0")
            list.add(HistoryModel(name, "Personal Purchase", "- $price", R.drawable.minion))
        }
        
        if (list.isEmpty()) {
            list.add(HistoryModel("No history yet", "Ayo belanja!", "Rp 0", R.drawable.minion))
        }
        return list
    }
}