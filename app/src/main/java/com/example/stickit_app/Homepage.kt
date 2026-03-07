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

        // 1. Greeting
        val tvHello = findViewById<TextView>(R.id.tvHello)
        tvHello?.text = "Hello, ${sessionManager.getName()}!"

        // 2. Navigasi History User (SEKARANG KE historyUser)
        findViewById<ImageView>(R.id.imageView)?.setOnClickListener {
            startActivity(Intent(this, historyUser::class.java))
        }

        // 3. Navigasi Kategori
        findViewById<LinearLayout>(R.id.btnMemeCategory)?.setOnClickListener {
            startActivity(Intent(this, meme::class.java))
        }
        findViewById<LinearLayout>(R.id.btnCharacterCategory)?.setOnClickListener {
            startActivity(Intent(this, character::class.java))
        }

        setupRecommendationFromInventory()

        // 4. Navigasi Bottom Bar
        val bottomNav = findViewById<LinearLayout>(R.id.bottomNavLayout)
        bottomNav?.getChildAt(1)?.setOnClickListener {
            startActivity(Intent(this, Keranjang::class.java))
        }
        bottomNav?.getChildAt(2)?.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        setupRecommendationFromInventory()
    }

    private fun setupRecommendationFromInventory() {
        val sharedPref = getSharedPreferences("InventoryData", MODE_PRIVATE)
        val count = sharedPref.getInt("INV_COUNT", 0)
        
        var snoopyName = "Snoopy Sticker"
        var snoopyPrice = "Rp 7.000"
        var snoopyImg = R.drawable.snoopy

        var finName = "Fin & Jake"
        var finPrice = "Rp 7.000"
        var finImg = R.drawable.fin___jake_

        for (i in 1..count) {
            val nameInStore = sharedPref.getString("INV_NAME_$i", "")
            if (nameInStore?.contains("Snoopy", true) == true) {
                snoopyName = nameInStore
                snoopyPrice = sharedPref.getString("INV_PRICE_$i", snoopyPrice) ?: snoopyPrice
                snoopyImg = sharedPref.getInt("INV_IMAGE_$i", snoopyImg)
            }
            if (nameInStore?.contains("Fin", true) == true) {
                finName = nameInStore
                finPrice = sharedPref.getString("INV_PRICE_$i", finPrice) ?: finPrice
                finImg = sharedPref.getInt("INV_IMAGE_$i", finImg)
            }
        }

        findViewById<TextView>(R.id.tvProductName1)?.text = snoopyName
        findViewById<TextView>(R.id.tvProductName2)?.text = finName

        findViewById<TextView>(R.id.btnViewSnoopy)?.setOnClickListener {
            passDataToAbout(snoopyName, snoopyPrice, snoopyImg)
        }
        findViewById<TextView>(R.id.btnViewFinJake)?.setOnClickListener {
            passDataToAbout(finName, finPrice, finImg)
        }
    }

    private fun passDataToAbout(name: String, price: String, imageRes: Int) {
        val intent = Intent(this, Aboutproduct::class.java).apply {
            putExtra("PRODUCT_NAME", name)
            putExtra("PRODUCT_PRICE", price)
            putExtra("PRODUCT_IMAGE", imageRes)
        }
        startActivity(intent)
    }
}