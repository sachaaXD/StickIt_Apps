package com.example.stickit_app

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream

class Addsticker : AppCompatActivity() {

    private var finalImagePath: String? = null
    private lateinit var ivPreview: ImageView

    // Launcher sakti buat ambil gambar dan kompres langsung biar ga crash
    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val imagePath = saveAndCompressImage(it)
            if (imagePath != null) {
                finalImagePath = imagePath
                // Tampilin gambar ke preview
                val bitmap = BitmapFactory.decodeFile(imagePath)
                ivPreview.setImageBitmap(bitmap)
                ivPreview.scaleType = ImageView.ScaleType.FIT_CENTER // Biar muat semua ukuran
            } else {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addsticker)

        ivPreview = findViewById(R.id.ivPreview)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val cvImport = findViewById<androidx.cardview.widget.CardView>(R.id.cvImportPhoto)
        val etName = findViewById<EditText>(R.id.etStickerName)
        val etPrice = findViewById<EditText>(R.id.etPrice)
        val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)
        val btnSave = findViewById<Button>(R.id.btnSaveSticker)

        val categories = arrayOf("Meme", "Animal", "Cute", "Character", "Circus", "Universe", "Spooky")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerCategory.adapter = adapter

        cvImport.setOnClickListener {
            getImage.launch("image/*")
        }

        btnBack.setOnClickListener { finish() }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val price = etPrice.text.toString().trim()
            val category = spinnerCategory.selectedItem.toString()

            if (name.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "Name and Price are required!", Toast.LENGTH_SHORT).show()
            } else {
                saveToInventory(name, price, category)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Fungsi buat copy dan kecilin ukuran gambar biar HP ga berat/crash
    private fun saveAndCompressImage(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val options = BitmapFactory.Options().apply { inSampleSize = 2 } // Kecilin resolusi jadi setengah
            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            
            val file = File(filesDir, "sticker_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // Kompres kualitas ke 80%
            
            outputStream.flush()
            outputStream.close()
            inputStream?.close()
            
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveToInventory(name: String, price: String, category: String) {
        val sharedPref = getSharedPreferences("InventoryData", MODE_PRIVATE)
        val editor = sharedPref.edit()

        val count = sharedPref.getInt("INV_COUNT", 0)
        val newIndex = count + 1

        editor.putString("INV_NAME_$newIndex", name)
        editor.putString("INV_PRICE_$newIndex", "Rp $price")
        editor.putString("INV_CAT_$newIndex", category)
        
        if (finalImagePath != null) {
            editor.putString("INV_IMAGE_URI_$newIndex", finalImagePath)
            editor.putInt("INV_IMAGE_$newIndex", 0) // Pake path absolut
        } else {
            editor.putInt("INV_IMAGE_$newIndex", R.drawable.minion)
        }

        editor.putInt("INV_COUNT", newIndex)
        editor.apply()

        Toast.makeText(this, "Sticker '$name' saved successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }
}