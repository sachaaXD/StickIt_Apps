package com.example.stickit_app

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class Keranjang : AppCompatActivity() {

    private lateinit var containerItems: LinearLayout
    private lateinit var tvTotal: TextView
    private val selectedItems = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keranjang)

        containerItems = findViewById(R.id.containerItems)
        tvTotal = findViewById(R.id.tvTotal)
        val btnDelete = findViewById<ImageView>(R.id.btnDelete)
        val btnCheckout = findViewById<Button>(R.id.btnCheckout)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        loadCartItems()

        btnBack.setOnClickListener { finish() }

        btnDelete.setOnClickListener {
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Pilih item dulu!", Toast.LENGTH_SHORT).show()
            } else {
                hapusItemTerpilih()
            }
        }

        btnCheckout.setOnClickListener {
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Pilih item yang mau dibeli!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, Beforepayment::class.java)
                intent.putExtra("TOTAL_HARGA", tvTotal.text.toString())
                intent.putExtra("NAMA_BARANG", getSelectedNames())
                intent.putIntegerArrayListExtra("SELECTED_INDEXES", ArrayList(selectedItems))
                startActivity(intent)
            }
        }
    }

    private fun loadCartItems() {
        containerItems.removeAllViews()
        val sharedPref = getSharedPreferences("CartData", MODE_PRIVATE)
        val count = sharedPref.getInt("ITEM_COUNT", 0)

        for (i in 1..count) {
            val name = sharedPref.getString("ITEM_NAME_$i", null) ?: continue
            val priceStr = sharedPref.getString("ITEM_PRICE_$i", "0")?.replace("$", "")?.replace("Rp", "")?.trim() ?: "0"
            val imageRes = sharedPref.getInt("ITEM_IMAGE_$i", 0)
            val imagePath = sharedPref.getString("ITEM_IMAGE_PATH_$i", null)

            val itemView = LayoutInflater.from(this).inflate(R.layout.item_cart, containerItems, false)
            val cbSelect = itemView.findViewById<CheckBox>(R.id.cbSelect)
            val ivItem = itemView.findViewById<ImageView>(R.id.ivItemImage)
            
            itemView.findViewById<TextView>(R.id.tvItemName).text = name
            itemView.findViewById<TextView>(R.id.tvItemPrice).text = "Rp $priceStr"

            // LOGIKA TAMPILIN GAMBAR (Uri/Path atau Resource)
            if (!imagePath.isNullOrEmpty()) {
                val imgFile = File(imagePath)
                if (imgFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    ivItem.setImageBitmap(bitmap)
                }
            } else if (imageRes != 0) {
                ivItem.setImageResource(imageRes)
            }

            cbSelect.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) selectedItems.add(i) else selectedItems.remove(i)
                updateTotal()
            }

            containerItems.addView(itemView)
        }
    }

    private fun updateTotal() {
        val sharedPref = getSharedPreferences("CartData", MODE_PRIVATE)
        var total = 0.0
        selectedItems.forEach { index ->
            val price = sharedPref.getString("ITEM_PRICE_$index", "0")?.replace("$", "")?.replace("Rp", "")?.trim()?.toDoubleOrNull() ?: 0.0
            total += price
        }
        tvTotal.text = "Rp ${String.format("%.0f", total)}"
    }

    private fun getSelectedNames(): String {
        val sharedPref = getSharedPreferences("CartData", MODE_PRIVATE)
        return selectedItems.joinToString(", ") { sharedPref.getString("ITEM_NAME_$it", "") ?: "" }
    }

    private fun hapusItemTerpilih() {
        val sharedPref = getSharedPreferences("CartData", MODE_PRIVATE)
        val editor = sharedPref.edit()

        selectedItems.forEach { index ->
            editor.remove("ITEM_NAME_$index")
            editor.remove("ITEM_PRICE_$index")
            editor.remove("ITEM_IMAGE_$index")
            editor.remove("ITEM_IMAGE_PATH_$index")
        }
        editor.apply()
        Toast.makeText(this, "Item dihapus!", Toast.LENGTH_SHORT).show()
        selectedItems.clear()
        loadCartItems()
        updateTotal()
    }
}