package com.example.stickit_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// Ini hanya file pendamping layout, tidak perlu logika berat
// karena semua kontrol dikendalikan dari Keranjang.kt
class item_keranjang_card : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pastikan nama layout-nya sesuai dengan file XML kartu kamu
        setContentView(R.layout.item_keranjang_card)
    }
}