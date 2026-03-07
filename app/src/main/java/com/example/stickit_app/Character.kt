package com.example.stickit_app

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class character : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }
        syncCharacterData()
    }

    override fun onResume() {
        super.onResume()
        syncCharacterData()
    }

    private fun syncCharacterData() {
        val sharedPref = getSharedPreferences("InventoryData", MODE_PRIVATE)
        val count = sharedPref.getInt("INV_COUNT", 0)
        
        // Slot ID sesuai layout activity_character.xml yang lo punya
        val buttonIds = arrayOf(R.id.tvB1, R.id.tvB2, R.id.tvB3, R.id.tvB4, R.id.tvB5, R.id.tvB6)
        val nameIds = arrayOf(R.id.tvN1, R.id.tvN2, R.id.tvN3, R.id.tvN4, R.id.tvN5, R.id.tvN6)
        // PERBAIKAN: Gunakan ID iv1 - iv6 sesuai XML lo
        val imageIds = arrayOf(R.id.iv1, R.id.iv2, R.id.iv3, R.id.iv4, R.id.iv5, R.id.iv6)

        var matchIndex = 0
        for (i in 1..count) {
            val cat = sharedPref.getString("INV_CAT_$i", "")
            if (cat == "Character" && matchIndex < buttonIds.size) {
                val name = sharedPref.getString("INV_NAME_$i", "") ?: ""
                val price = sharedPref.getString("INV_PRICE_$i", "") ?: ""
                val imageRes = sharedPref.getInt("INV_IMAGE_$i", 0)
                val imagePath = sharedPref.getString("INV_IMAGE_URI_$i", null)

                // 1. Update Gambar
                try {
                    val ivSticker = findViewById<ImageView>(imageIds[matchIndex])
                    if (ivSticker != null) {
                        if (!imagePath.isNullOrEmpty()) {
                            val bitmap = BitmapFactory.decodeFile(imagePath)
                            ivSticker.setImageBitmap(bitmap)
                        } else if (imageRes != 0) {
                            ivSticker.setImageResource(imageRes)
                        }
                    }
                } catch (e: Exception) { }

                // 2. Update Nama
                try {
                    val tvName = findViewById<TextView>(nameIds[matchIndex])
                    if (tvName != null) tvName.text = name
                } catch (e: Exception) { }

                // 3. Logika Klik
                val btnView = findViewById<TextView>(buttonIds[matchIndex])
                btnView?.setOnClickListener {
                    passDataToAbout(name, price, imageRes, imagePath)
                }
                
                // Pastikan Layout (Card) kelihatan
                (btnView?.parent?.parent as? View)?.visibility = View.VISIBLE
                
                matchIndex++
            }
        }
        
        // Sembunyikan slot sisa yang kosong
        for (i in matchIndex until buttonIds.size) {
            findViewById<TextView>(buttonIds[i])?.parent?.parent?.let { (it as View).visibility = View.GONE }
        }
    }

    private fun passDataToAbout(name: String, price: String, imageRes: Int, imagePath: String?) {
        val intent = Intent(this, Aboutproduct::class.java).apply {
            putExtra("PRODUCT_NAME", name)
            putExtra("PRODUCT_PRICE", price)
            if (!imagePath.isNullOrEmpty()) {
                putExtra("PRODUCT_IMAGE_PATH", imagePath)
            } else {
                putExtra("PRODUCT_IMAGE", imageRes)
            }
        }
        startActivity(intent)
    }
}