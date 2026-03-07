package com.example.stickit_app

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class meme : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme)

        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val rvMeme = findViewById<RecyclerView>(R.id.rvMemeUser)
        if (rvMeme != null) {
            rvMeme.layoutManager = GridLayoutManager(this, 2)
            val dataMeme = loadMemeFromInventory()
            rvMeme.adapter = StickerUserAdapter(dataMeme)
        }
    }

    private fun loadMemeFromInventory(): List<StickerModel> {
        val sharedPref = getSharedPreferences("InventoryData", MODE_PRIVATE)
        val list = mutableListOf<StickerModel>()
        val count = sharedPref.getInt("INV_COUNT", 0)

        for (i in count downTo 1) {
            val cat = sharedPref.getString("INV_CAT_$i", "")
            // FILTER KETAT: Cuma ambil yang kategori Meme
            if (cat?.equals("Meme", ignoreCase = true) == true) {
                val name = sharedPref.getString("INV_NAME_$i", "") ?: ""
                val price = sharedPref.getString("INV_PRICE_$i", "") ?: ""
                val imageRes = sharedPref.getInt("INV_IMAGE_$i", 0)
                list.add(StickerModel(name, price, imageRes, "Meme"))
            }
        }
        return list
    }
}