package com.example.stickit_app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StickerManagemment : AppCompatActivity() {

    private lateinit var adapter: StickerAdapter
    private lateinit var etSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stickermanagement)

        etSearch = findViewById(R.id.etSearchStickers)
        findViewById<ImageButton>(R.id.btnBack)?.setOnClickListener { finish() }
        findViewById<ImageButton>(R.id.btnAddSticker)?.setOnClickListener {
            startActivity(Intent(this, Addsticker::class.java))
        }

        setupRecyclerView()
        setupSearch()
    }

    override fun onResume() {
        super.onResume()
        // Reload data from storage to refresh the list (if any new items added/edited)
        val stickers = loadStickersFromStorage().toMutableList()
        val rvStickers = findViewById<RecyclerView>(R.id.rvStickers)
        
        adapter = StickerAdapter(stickers)
        rvStickers.adapter = adapter
        
        // Re-apply filter if there is already text in the search box
        val query = etSearch.text.toString()
        if (query.isNotEmpty()) {
            adapter.filter(query)
        }
    }

    private fun setupRecyclerView() {
        val rvStickers = findViewById<RecyclerView>(R.id.rvStickers)
        rvStickers.layoutManager = LinearLayoutManager(this)
        adapter = StickerAdapter(loadStickersFromStorage().toMutableList())
        rvStickers.adapter = adapter
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
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