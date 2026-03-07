package com.example.stickit_app

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class Homeadmin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homeadmin)

        val sessionManager = SessionManager(this)

        // 1. Kartu Sticker Management
        val cardManageProducts = findViewById<CardView>(R.id.cardManageProducts)
        cardManageProducts?.setOnClickListener {
            startActivity(Intent(this, StickerManagemment::class.java))
        }

        // 2. Kartu Order Tracking
        val cardOrders = findViewById<CardView>(R.id.cardOrders)
        cardOrders?.setOnClickListener {
            startActivity(Intent(this, Orders::class.java))
        }

        // 3. SAMBUNGIN BOTTOM NAV (Hanya 2 Tombol: Home & Settings)
        val bottomNav = findViewById<CardView>(R.id.bottomNavContainer)
        val navLayout = bottomNav?.getChildAt(0) as? LinearLayout
        
        // Index 0 = Home, Index 1 = Settings
        val btnSettingsNav = navLayout?.getChildAt(1)

        btnSettingsNav?.setOnClickListener {
            startActivity(Intent(this, settingAdmin::class.java))
        }
    }
}