package com.example.stickit_app

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.io.File

class Aboutproduct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutproduct)

        val ivProduct = findViewById<ImageView>(R.id.ivProduct)
        val tvProductName = findViewById<TextView>(R.id.tvProductName)
        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val tvTotalPrice = findViewById<TextView>(R.id.tvTotalPrice)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnAddToCart = findViewById<MaterialButton>(R.id.btnAddToCart)
        val btnCartIcon = findViewById<ImageView>(R.id.btnCartIcon)

        val productName = intent.getStringExtra("STICKER_NAME") ?: intent.getStringExtra("PRODUCT_NAME") ?: "Sticker"
        val productPrice = intent.getStringExtra("STICKER_PRICE") ?: intent.getStringExtra("PRODUCT_PRICE") ?: "0"
        val productImage = intent.getIntExtra("STICKER_IMAGE", intent.getIntExtra("PRODUCT_IMAGE", 0))
        val productImagePath = intent.getStringExtra("PRODUCT_IMAGE_PATH")

        tvProductName.text = productName
        val displayPrice = if (productPrice.startsWith("Rp")) productPrice else "Rp $productPrice"
        tvPrice.text = displayPrice
        tvTotalPrice.text = displayPrice

        if (!productImagePath.isNullOrEmpty()) {
            val imgFile = File(productImagePath)
            if (imgFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                ivProduct.setImageBitmap(bitmap)
            }
        } else if (productImage != 0) {
            ivProduct.setImageResource(productImage)
        }

        fun simpanKeKeranjang() {
            val sharedPref = getSharedPreferences("CartData", MODE_PRIVATE)
            val editor = sharedPref.edit()
            val currentCount = sharedPref.getInt("ITEM_COUNT", 0)
            val newCount = currentCount + 1

            editor.putString("ITEM_NAME_$newCount", productName)
            editor.putString("ITEM_PRICE_$newCount", productPrice)
            if (!productImagePath.isNullOrEmpty()) {
                editor.putString("ITEM_IMAGE_PATH_$newCount", productImagePath)
            } else {
                editor.putInt("ITEM_IMAGE_$newCount", productImage)
            }
            editor.putInt("ITEM_COUNT", newCount)
            editor.apply()
            
            Toast.makeText(this, "$productName added to basket!", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener { finish() }

        btnCartIcon?.setOnClickListener {
            simpanKeKeranjang()
            val intent = Intent(this, Keranjang::class.java)
            startActivity(intent)
        }

        btnAddToCart.setOnClickListener {
            // LANGSUNG PINDAH TANPA SIMPAN KE KERANJANG
            val intent = Intent(this, Beforepayment::class.java)
            intent.putExtra("NAMA_BARANG", productName)
            intent.putExtra("TOTAL_HARGA", displayPrice)
            if (!productImagePath.isNullOrEmpty()) {
                intent.putExtra("GAMBAR_BARANG_PATH", productImagePath)
            } else {
                intent.putExtra("GAMBAR_BARANG", productImage)
            }
            startActivity(intent)
        }
    }
}