package com.example.stickit_app

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Orders : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack?.setOnClickListener { finish() }

        val rvOrders = findViewById<RecyclerView>(R.id.rvOrders)
        rvOrders.layoutManager = LinearLayoutManager(this)

        // LOAD DATA SEMUA TRANSAKSI (Khusus Admin)
        val allOrders = loadAdminAllOrders()
        rvOrders.adapter = HistoryAdapter(allOrders)
    }

    private fun loadAdminAllOrders(): List<HistoryModel> {
        val list = mutableListOf<HistoryModel>()
        // Baca dari buku besar Admin (AdminHistory)
        val sharedPref = getSharedPreferences("AdminHistory", MODE_PRIVATE)
        val count = sharedPref.getInt("COUNT", 0)

        for (i in count downTo 1) {
            val name = sharedPref.getString("H_NAME_$i", null) ?: continue
            val price = sharedPref.getString("H_PRICE_$i", "Rp 0")
            val user = sharedPref.getString("H_USER_$i", "Unknown User")
            
            // TAMPILKAN NAMA PEMBELI DI BAWAH NAMA STIKER
            list.add(HistoryModel(name, "Buyer: $user", "- $price", R.drawable.minion))
        }

        if (list.isEmpty()) {
            list.add(HistoryModel("No Transactions", "Wait for users to shop", "Rp 0", R.drawable.minion))
        }
        return list
    }
}