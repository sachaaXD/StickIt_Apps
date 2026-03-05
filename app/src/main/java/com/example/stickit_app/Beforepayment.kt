package com.example.stickit_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class Beforepayment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beforepayment)

        val btnClose = findViewById<ImageView>(R.id.btnClose)
        val btnPayNow = findViewById<AppCompatButton>(R.id.btnPayNow)
        val tvOrderNames = findViewById<TextView>(R.id.tvOrderNames)
        val tvOrderTotal = findViewById<TextView>(R.id.tvOrderTotal)

        // 1. TERIMA DATA DARI KERANJANG
        val namaBarangTerpilih = intent.getStringExtra("NAMA_BARANG") ?: "No item selected"
        val totalHargaTerpilih = intent.getStringExtra("TOTAL_HARGA") ?: "$0.00"

        // 2. TAMPILKAN KE LAYOUT
        tvOrderNames.text = namaBarangTerpilih
        tvOrderTotal.text = totalHargaTerpilih

        // Tombol Close
        btnClose.setOnClickListener {
            finish()
        }

        // Tombol Pay Now -> Langsung ke Afterpayment
        // Di dalam btnPayNow.setOnClickListener
        btnPayNow.setOnClickListener {
            val intent = Intent(this, Afterpayment::class.java)

            // Kirim data nama dan harga (buat tampilan)
            intent.putExtra("FINAL_NAME", tvOrderNames.text.toString())
            intent.putExtra("FINAL_PRICE", tvOrderTotal.text.toString())

            // KIRIM DAFTAR INDEX (ambil dari intent yang dikirim Keranjang tadi)
            val selectedIndexes = getIntent().getIntegerArrayListExtra("SELECTED_INDEXES")
            intent.putIntegerArrayListExtra("SELECTED_INDEXES", selectedIndexes)

            startActivity(intent)
        }

    }
}