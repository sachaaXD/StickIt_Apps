package com.example.stickit_app

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Editsticker : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editsticker)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val etName = findViewById<EditText>(R.id.etStickerNameEdit)
        val etPrice = findViewById<EditText>(R.id.etPriceEdit)
        val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategoryEdit)
        val btnEditSubmit = findViewById<Button>(R.id.btnEditSticker)

        // 1. TERIMA DATA DARI ADAPTER
        val originalName = intent.getStringExtra("STICKER_NAME") ?: ""
        val price = intent.getStringExtra("STICKER_PRICE") ?: ""
        val category = intent.getStringExtra("STICKER_CATEGORY") ?: "Character"

        // 2. ISI FORM
        etName.setText(originalName)
        etPrice.setText(price.replace("Rp", "").replace(".", "").replace(",", "").trim())

        val categories = arrayOf("Meme", "Animal", "Cute", "Character", "Circus", "Universe", "Spooky")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerCategory.adapter = adapter
        spinnerCategory.setSelection(categories.indexOf(category).let { if (it == -1) 3 else it })

        btnBack.setOnClickListener { finish() }

        // 3. PROSES SAVE PERMANEN KE INVENTORI
        btnEditSubmit.setOnClickListener {
            val newName = etName.text.toString().trim()
            val newPrice = etPrice.text.toString().trim()
            val newCat = spinnerCategory.selectedItem.toString()

            if (newName.isEmpty() || newPrice.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show()
            } else {
                val sharedPref = getSharedPreferences("InventoryData", MODE_PRIVATE)
                val editor = sharedPref.edit()
                
                val count = sharedPref.getInt("INV_COUNT", 0)
                var isUpdated = false

                // Cari stiker berdasarkan nama aslinya di memori
                for (i in 1..count) {
                    if (sharedPref.getString("INV_NAME_$i", "") == originalName) {
                        editor.putString("INV_NAME_$i", newName)
                        editor.putString("INV_PRICE_$i", "Rp $newPrice")
                        editor.putString("INV_CAT_$i", newCat)
                        isUpdated = true
                        break
                    }
                }

                if (isUpdated) {
                    editor.apply()
                    Toast.makeText(this, "Sticker Updated Permanently!", Toast.LENGTH_SHORT).show()
                    finish() // Balik ke list
                } else {
                    Toast.makeText(this, "Sticker not found in storage!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}