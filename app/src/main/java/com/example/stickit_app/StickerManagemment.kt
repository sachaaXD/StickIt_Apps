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
        setupRecyclerView() // Refresh data setiap balik dari Edit/Add
    }

    private fun setupRecyclerView() {
        val rvStickers = findViewById<RecyclerView>(R.id.rvStickers)
        rvStickers.layoutManager = LinearLayoutManager(this)
        
        val dataStickers = loadStickersFromStorage()
        rvStickers.adapter = StickerAdapter(dataStickers.toMutableList())
    }

    private fun loadStickersFromStorage(): List<StickerModel> {
        val sharedPref = getSharedPreferences("InventoryData", MODE_PRIVATE)
        val list = mutableListOf<StickerModel>()
        
        // Hanya inisialisasi jika benar-benar kosong (pertama kali install)
        if (!sharedPref.contains("INV_COUNT")) {
            initDefaultInventory()
        }

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

    private fun initDefaultInventory() {
        val sharedPref = getSharedPreferences("InventoryData", MODE_PRIVATE)
        val editor = sharedPref.edit()
        
        val defaults = mutableListOf<StickerModel>()
        // MEME (8)
        defaults.add(StickerModel("Uhhh Dog", "Rp 5.000", R.drawable._let_me_know__ahh_dog, "Meme"))
        defaults.add(StickerModel("Pegipegipegih", "Rp 5.000", R.drawable.pegi_pegi_pegi, "Meme"))
        defaults.add(StickerModel("Heumzz", "Rp 5.000", R.drawable.resource__, "Meme"))
        defaults.add(StickerModel("Tenank", "Rp 5.000", R.drawable.memes, "Meme"))
        defaults.add(StickerModel("Reaksiku", "Rp 5.000", R.drawable.my_frenemy___as_10_____3__, "Meme"))
        defaults.add(StickerModel("Grrrrr", "Rp 5.000", R.drawable.how_to_create_goofy_drawings__fun_and_easy_step_by_step_guide___fascinate_names, "Meme"))
        defaults.add(StickerModel("Mikir kids", "Rp 5.000", R.drawable.thinkbruh, "Meme"))
        defaults.add(StickerModel("Wleee", "Rp 5.000", R.drawable.rockkkk, "Meme"))
        // CHARACTER (6)
        defaults.add(StickerModel("Belle", "Rp 5.000", R.drawable.design_a_bedroom_and_we_ll_tell_you_which_disney_princess_you_re_most_like, "Character"))
        defaults.add(StickerModel("Aang", "Rp 5.000", R.drawable._3_unpopular__, "Character"))
        defaults.add(StickerModel("Venellope", "Rp 5.000", R.drawable.wreck_it_ralph_vanellope_von_schweetz, "Character"))
        defaults.add(StickerModel("Minion", "Rp 5.000", R.drawable.minionsss, "Character"))
        defaults.add(StickerModel("Olaf", "Rp 5.000", R.drawable.moviegoers_love__, "Character"))
        defaults.add(StickerModel("Moana", "Rp 5.000", R.drawable.hey_maui__it_s_been_a_little_while__i_i_i_don_t_know_where_you_are_but__takes_a_deep_breath__i_could_really_use_your_help__, "Character"))
        // RECOMMENDATION (2)
        defaults.add(StickerModel("Snoopy Sticker", "Rp 7.000", R.drawable.snoopy, "Animal"))
        defaults.add(StickerModel("Fin & Jake", "Rp 7.000", R.drawable.fin___jake_, "Character"))

        editor.putInt("INV_COUNT", defaults.size)
        defaults.forEachIndexed { index, sticker ->
            val i = index + 1
            editor.putString("INV_NAME_$i", sticker.name)
            editor.putString("INV_PRICE_$i", sticker.price)
            editor.putInt("INV_IMAGE_$i", sticker.image)
            editor.putString("INV_CAT_$i", sticker.category)
        }
        editor.apply()
    }
}