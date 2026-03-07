package com.example.stickit_app

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class character : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }
        
        // 1. Pastikan data awal Character ada jika memori kosong
        initCharacterInventory()
        // 2. Tampilkan daftar stiker
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        // Refresh data biar stiker baru dari Admin langsung muncul
        setupRecyclerView()
    }

    private fun initCharacterInventory() {
        val sharedPref = getSharedPreferences("InventoryData", MODE_PRIVATE)
        if (!sharedPref.getBoolean("IS_CHAR_INIT", false)) {
            val editor = sharedPref.edit()
            val charDefaults = listOf(
                StickerModel("Belle", "Rp 5.000", R.drawable.design_a_bedroom_and_we_ll_tell_you_which_disney_princess_you_re_most_like, "Character"),
                StickerModel("Aang", "Rp 5.000", R.drawable._3_unpopular__, "Character"),
                StickerModel("Venellope", "Rp 5.000", R.drawable.wreck_it_ralph_vanellope_von_schweetz, "Character"),
                StickerModel("Minion", "Rp 5.000", R.drawable.minionsss, "Character"),
                StickerModel("Olaf", "Rp 5.000", R.drawable.moviegoers_love__, "Character"),
                StickerModel("Moana", "Rp 5.000", R.drawable.hey_maui__it_s_been_a_little_while__i_i_i_don_t_know_where_you_are_but__takes_a_deep_breath__i_could_really_use_your_help__, "Character")
            )
            
            var currentCount = sharedPref.getInt("INV_COUNT", 0)
            charDefaults.forEach { s ->
                currentCount++
                editor.putString("INV_NAME_$currentCount", s.name)
                editor.putString("INV_PRICE_$currentCount", s.price)
                editor.putInt("INV_IMAGE_$currentCount", s.image)
                editor.putString("INV_CAT_$currentCount", s.category)
            }
            editor.putInt("INV_COUNT", currentCount)
            editor.putBoolean("IS_CHAR_INIT", true)
            editor.apply()
        }
    }

    private fun setupRecyclerView() {
        val rvCharacter = findViewById<RecyclerView>(R.id.rvCharacterUser)
        if (rvCharacter != null) {
            rvCharacter.layoutManager = GridLayoutManager(this, 2)
            val dataCharacter = loadCharacterFromInventory()
            rvCharacter.adapter = StickerUserAdapter(dataCharacter)
        }
    }

    private fun loadCharacterFromInventory(): List<StickerModel> {
        val sharedPref = getSharedPreferences("InventoryData", MODE_PRIVATE)
        val list = mutableListOf<StickerModel>()
        val count = sharedPref.getInt("INV_COUNT", 0)

        // LOOP TERBALIK: Biar stiker baru (kucong dll) muncul paling depan
        for (i in count downTo 1) {
            val cat = sharedPref.getString("INV_CAT_$i", "")
            // Pengecekan kategori dibuat lebih kuat (ignore case)
            if (cat?.equals("Character", ignoreCase = true) == true) {
                val name = sharedPref.getString("INV_NAME_$i", "") ?: ""
                val price = sharedPref.getString("INV_PRICE_$i", "") ?: ""
                val imageRes = sharedPref.getInt("INV_IMAGE_$i", 0)
                list.add(StickerModel(name, price, imageRes, "Character"))
            }
        }
        return list
    }
}