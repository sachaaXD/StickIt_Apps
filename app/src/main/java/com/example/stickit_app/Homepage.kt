package com.example.stickit_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Homepage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        val sessionManager = SessionManager(this)

        // 1. Greeting (Ambil nama dari session)
        val tvHello = findViewById<TextView>(R.id.tvHello)
        tvHello?.text = "Hello, ${sessionManager.getName()}!"

        // 2. Navigasi History (Icon Clipboard di sebelah kanan profile)
        findViewById<ImageView>(R.id.imageView)?.setOnClickListener {
            startActivity(Intent(this, History::class.java))
        }

        // 3. Navigasi Kategori
        findViewById<LinearLayout>(R.id.btnMemeCategory)?.setOnClickListener {
            startActivity(Intent(this, meme::class.java))
        }
        findViewById<LinearLayout>(R.id.btnCharacterCategory)?.setOnClickListener {
            startActivity(Intent(this, character::class.java))
        }

        // 4. Tombol View More Rekomendasi (Ke AboutProduct)
        findViewById<TextView>(R.id.btnViewSnoopy)?.setOnClickListener {
            passDataToAbout("Snoopy", "7000", R.drawable.snoopy)
        }
        findViewById<TextView>(R.id.btnViewFinJake)?.setOnClickListener {
            passDataToAbout("Fin & Jake", "7000", R.drawable.fin___jake_)
        }

        // 5. Navigasi Bottom Bar
        val bottomNav = findViewById<LinearLayout>(R.id.bottomNavLayout)

        // Klik Basket (Tengah) -> Ke Keranjang
        bottomNav?.getChildAt(1)?.setOnClickListener {
            startActivity(Intent(this, Keranjang::class.java))
        }

        // Klik Setting (Paling Kanan) -> Ke Profile
        bottomNav?.getChildAt(2)?.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }
    }

    // Fungsi bantu buat lempar data ke halaman detail
    private fun passDataToAbout(name: String, price: String, imageRes: Int) {
        val intent = Intent(this, Aboutproduct::class.java).apply {
            putExtra("PRODUCT_NAME", name)
            putExtra("PRODUCT_PRICE", price)
            putExtra("PRODUCT_IMAGE", imageRes)
        }
        startActivity(intent)
    }
}