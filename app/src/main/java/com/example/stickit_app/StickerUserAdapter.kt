package com.example.stickit_app

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class StickerUserAdapter(private val listStickers: List<StickerModel>) :
    RecyclerView.Adapter<StickerUserAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivSticker: ImageView = view.findViewById(R.id.ivUserSticker)
        val tvName: TextView = view.findViewById(R.id.tvUserName)
        val tvPrice: TextView = view.findViewById(R.id.tvUserPrice)
        val btnViewMore: TextView = view.findViewById(R.id.btnViewMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sticker_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sticker = listStickers[position]

        holder.tvName.text = sticker.name
        holder.tvPrice.text = sticker.price.replace("Rp", "").trim()

        // Handle Image (Internal Path or Resource)
        val sharedPref = holder.itemView.context.getSharedPreferences("InventoryData", android.content.Context.MODE_PRIVATE)
        val count = sharedPref.getInt("INV_COUNT", 0)
        var imagePath: String? = null
        
        // Find image path by name (since we don't have IDs)
        for (i in 1..count) {
            if (sharedPref.getString("INV_NAME_$i", "") == sticker.name) {
                imagePath = sharedPref.getString("INV_IMAGE_URI_$i", null)
                break
            }
        }

        if (!imagePath.isNullOrEmpty()) {
            val imgFile = File(imagePath)
            if (imgFile.exists()) {
                val options = BitmapFactory.Options().apply { inSampleSize = 2 }
                val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath, options)
                holder.ivSticker.setImageBitmap(bitmap)
            } else {
                holder.ivSticker.setImageResource(sticker.image)
            }
        } else {
            holder.ivSticker.setImageResource(sticker.image)
        }

        holder.btnViewMore.setOnClickListener {
            val intent = Intent(holder.itemView.context, Aboutproduct::class.java).apply {
                putExtra("PRODUCT_NAME", sticker.name)
                putExtra("PRODUCT_PRICE", sticker.price)
                if (!imagePath.isNullOrEmpty()) {
                    putExtra("PRODUCT_IMAGE_PATH", imagePath)
                } else {
                    putExtra("PRODUCT_IMAGE", sticker.image)
                }
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listStickers.size
}