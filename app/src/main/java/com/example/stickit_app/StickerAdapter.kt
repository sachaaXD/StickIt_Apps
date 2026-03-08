package com.example.stickit_app

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.Locale

class StickerAdapter(private var listStickers: MutableList<StickerModel>) :
    RecyclerView.Adapter<StickerAdapter.ViewHolder>() {

    private var listStickersFull: List<StickerModel> = ArrayList(listStickers)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.ivStickerImage)
        val tvName: TextView = view.findViewById(R.id.tvStickerName)
        val tvPrice: TextView = view.findViewById(R.id.tvStickerPrice)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEditSticker)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteSticker)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sticker_manage, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sticker = listStickers[position]
        val context = holder.itemView.context

        holder.tvName.text = sticker.name
        holder.tvPrice.text = sticker.price

        // --- LOGIKA LOAD GAMBAR ANTI-CRASH ---
        val imagePath = findImagePathByName(context, sticker.name)
        
        if (!imagePath.isNullOrEmpty()) {
            val imgFile = File(imagePath)
            if (imgFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                holder.ivImage.setImageBitmap(bitmap)
            } else {
                holder.ivImage.setImageResource(sticker.image)
            }
        } else {
            // Jika sticker bawaan, pake resource ID (Minion/Snoopy dll)
            if (sticker.image != 0) {
                holder.ivImage.setImageResource(sticker.image)
            } else {
                holder.ivImage.setImageResource(R.drawable.minion)
            }
        }

        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, Editsticker::class.java)
            intent.putExtra("STICKER_NAME", sticker.name)
            intent.putExtra("STICKER_PRICE", sticker.price)
            intent.putExtra("STICKER_IMAGE", sticker.image)
            intent.putExtra("STICKER_CATEGORY", sticker.category)
            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            deleteFromInventory(context, sticker.name)
            // Remove from both lists to keep them in sync
            val removedItem = listStickers[position]
            listStickers.removeAt(position)
            
            val fullList = listStickersFull.toMutableList()
            fullList.remove(removedItem)
            listStickersFull = fullList
            
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, listStickers.size)
            Toast.makeText(context, "${sticker.name} deleted!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun findImagePathByName(context: Context, name: String): String? {
        val sharedPref = context.getSharedPreferences("InventoryData", Context.MODE_PRIVATE)
        val count = sharedPref.getInt("INV_COUNT", 0)
        for (i in 1..count) {
            if (sharedPref.getString("INV_NAME_$i", "") == name) {
                return sharedPref.getString("INV_IMAGE_URI_$i", null)
            }
        }
        return null
    }

    private fun deleteFromInventory(context: Context, name: String) {
        val sharedPref = context.getSharedPreferences("InventoryData", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val count = sharedPref.getInt("INV_COUNT", 0)
        for (i in 1..count) {
            if (sharedPref.getString("INV_NAME_$i", "") == name) {
                editor.remove("INV_NAME_$i")
                editor.remove("INV_PRICE_$i")
                editor.remove("INV_IMAGE_$i")
                editor.remove("INV_IMAGE_URI_$i")
                editor.remove("INV_CAT_$i")
                editor.apply()
                break
            }
        }
    }

    override fun getItemCount(): Int = listStickers.size

    fun filter(text: String) {
        val filterPattern = text.lowercase(Locale.ROOT).trim()
        listStickers.clear()
        
        if (filterPattern.isEmpty()) {
            listStickers.addAll(listStickersFull)
        } else {
            for (item in listStickersFull) {
                if (item.name.lowercase(Locale.ROOT).contains(filterPattern) || 
                    item.category.lowercase(Locale.ROOT).contains(filterPattern)) {
                    listStickers.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }
}