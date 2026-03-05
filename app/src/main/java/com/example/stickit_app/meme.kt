package com.example.stickit_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class meme : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme)

        // 1. Tombol Back untuk kembali ke Homepage
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack?.setOnClickListener {
            finish()
        }

        // 2. Navigasi "View More" untuk ke-8 Item Meme

        // Item 1: Uhhh
        findViewById<TextView>(R.id.tvB1)?.setOnClickListener {
            passDataToAbout("Uhhh Dog", "5000", R.drawable._let_me_know__ahh_dog)
        }

        // Item 2: Pegipegipegih
        findViewById<TextView>(R.id.tvB2)?.setOnClickListener {
            passDataToAbout("Pegipegipegih", "5000", R.drawable.pegi_pegi_pegi)
        }

        // Item 3: Heumzz
        findViewById<TextView>(R.id.tvB3)?.setOnClickListener {
            passDataToAbout("Heumzz", "5000", R.drawable.resource__)
        }

        // Item 4: Tenank
        findViewById<TextView>(R.id.tvB4)?.setOnClickListener {
            passDataToAbout("Tenank", "5000", R.drawable.memes)
        }

        // Item 5: Reaksiku
        findViewById<TextView>(R.id.tvB5)?.setOnClickListener {
            passDataToAbout("Reaksiku", "5000", R.drawable.my_frenemy___as_10_____3__)
        }

        // Item 6: Grrrrr
        findViewById<TextView>(R.id.tvB6)?.setOnClickListener {
            passDataToAbout("Grrrrr", "5000", R.drawable.how_to_create_goofy_drawings__fun_and_easy_step_by_step_guide___fascinate_names)
        }

        // Item 7: Mikir kids
        findViewById<TextView>(R.id.tvB7)?.setOnClickListener {
            passDataToAbout("Mikir kids", "5000", R.drawable.thinkbruh)
        }

        // Item 8: Wleee
        findViewById<TextView>(R.id.tvB8)?.setOnClickListener {
            passDataToAbout("Wleee", "5000", R.drawable.rockkkk)
        }
    }

    /**
     * Fungsi sakti untuk kirim paket data ke Aboutproduct
     */
    private fun passDataToAbout(name: String, price: String, imageRes: Int) {
        val intent = Intent(this, Aboutproduct::class.java)
        intent.putExtra("PRODUCT_NAME", name)
        intent.putExtra("PRODUCT_PRICE", "Rp $price")
        intent.putExtra("PRODUCT_IMAGE", imageRes)
        startActivity(intent)
    }
}