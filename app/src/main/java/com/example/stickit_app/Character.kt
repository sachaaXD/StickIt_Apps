package com.example.stickit_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class character : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        // 1. Handle Tombol Back (Kembali ke Homepage)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack?.setOnClickListener {
            finish()
        }

        // 2. Navigasi ke Aboutproduct untuk setiap Karakter

        // --- Card 1: Belle ---
        findViewById<TextView>(R.id.tvB1)?.setOnClickListener {
            passDataToAbout("Belle", "5000", R.drawable.design_a_bedroom_and_we_ll_tell_you_which_disney_princess_you_re_most_like)
        }

        // --- Card 2: Aang ---
        findViewById<TextView>(R.id.tvB2)?.setOnClickListener {
            passDataToAbout("Aang", "5000", R.drawable._3_unpopular__)
        }

        // --- Card 3: Venellope ---
        findViewById<TextView>(R.id.tvB3)?.setOnClickListener {
            passDataToAbout("Venellope", "5000", R.drawable.wreck_it_ralph_vanellope_von_schweetz)
        }

        // --- Card 4: Minion ---
        findViewById<TextView>(R.id.tvB4)?.setOnClickListener {
            passDataToAbout("Minion", "5000", R.drawable.minionsss)
        }

        // --- Card 5: Olaf ---
        findViewById<TextView>(R.id.tvB5)?.setOnClickListener {
            passDataToAbout("Olaf", "5000", R.drawable.moviegoers_love__)
        }

        // --- Card 6: Moana ---
        findViewById<TextView>(R.id.tvB6)?.setOnClickListener {
            passDataToAbout("Moana", "5000", R.drawable.hey_maui__it_s_been_a_little_while__i_i_i_don_t_know_where_you_are_but__takes_a_deep_breath__i_could_really_use_your_help__)
        }
    }

    /**
     * Fungsi pembantu agar kode lebih rapi (tidak mengulang-ulang Intent)
     */
    private fun passDataToAbout(name: String, price: String, imageRes: Int) {
        val intent = Intent(this, Aboutproduct::class.java)
        intent.putExtra("PRODUCT_NAME", name)
        intent.putExtra("PRODUCT_PRICE", "Rp $price")
        intent.putExtra("PRODUCT_IMAGE", imageRes)
        startActivity(intent)
    }
}