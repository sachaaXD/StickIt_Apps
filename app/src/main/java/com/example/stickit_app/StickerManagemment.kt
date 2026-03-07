package com.example.stickit_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StickerManagemment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stickermanagemrnt)

        findViewById<ImageButton>(R.id.btnBack)?.setOnClickListener { finish() }
        findViewById<ImageButton>(R.id.btnAddSticker)?.setOnClickListener {
            startActivity(Intent(this, Addsticker::class.java))
        }

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val rvStickers = findViewById<RecyclerView>(R.id.rvStickers)
        rvStickers.layoutManager = LinearLayoutManager(this)
        rvStickers.adapter = StickerAdapter(loadStickersFromStorage().toMutableList())
    }

    private fun loadStickersFromStorage(): List<StickerModel> {
        val sharedPref = getSharedPreferences("InventoryData", MODE_PRIVATE)
        val list = mutableListOf<StickerModel>()
        val count = sharedPref.getInt("INV_COUNT", 0)

        for (i in 1..count) {
            val name = sharedPref.getString("INV_NAME_$i", null) ?: continue
            val price = sharedPref.getString("INV_PRICE_$i", "Rp 0")
            val image = sharedPref.getInt("INV_IMAGE_$i", R.drawable.minion)
            val cat = sharedPref.getString("INV_CAT_$i", "Meme")
            list.add(StickerModel(name, price!!, image, cat!!))
        }
        return list
    }
}