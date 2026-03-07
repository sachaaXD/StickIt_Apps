package com.example.stickit_app

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionManager = SessionManager(this)
        val isLogout = intent.getBooleanExtra("LOGOUT", false)

        if (sessionManager.isLoggedIn() && !isLogout) {
            // Pastikan data inventory siap
            initInventoryIfNeeded()
            
            if (sessionManager.getRole() == "admin") {
                startActivity(Intent(this, Homeadmin::class.java))
            } else {
                startActivity(Intent(this, Homepage::class.java))
            }
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val etUsername = findViewById<EditText>(R.id.etUsernameLogin)
        val etPassword = findViewById<EditText>(R.id.etPasswordLogin)
        val btnLogin = findViewById<AppCompatButton>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password harus diisi!", Toast.LENGTH_SHORT).show()
            } else {
                if (username.lowercase() == "asha") {
                    initInventoryIfNeeded() // Isi data stiker
                    sessionManager.saveSession(1, "Asha", "asha@gmail.com", "admin")
                    startActivity(Intent(this, Homeadmin::class.java))
                    finish()
                } else {
                    processLogin(username, password)
                }
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

    private fun initInventoryIfNeeded() {
        val sharedPref = getSharedPreferences("InventoryData", MODE_PRIVATE)
        if (!sharedPref.getBoolean("IS_INITIALIZED", false)) {
            val editor = sharedPref.edit()
            val defaults = mutableListOf<StickerModel>()
            
            // MEME (8)
            defaults.add(StickerModel("Uhhh Dog", "Rp 5.000", R.drawable._let_me_know__ahh_dog, "Meme"))
            defaults.add(StickerModel("Pegipegipegih", "Rp 5.000", R.drawable.pegi_pegi_pegi, "Meme"))
            defaults.add(StickerModel("Heumzz", "Rp 5.000", R.drawable.resource__, "Meme"))
            defaults.add(StickerModel("Tenank", "Rp 5.000", R.drawable.memes, "Meme"))
            defaults.add(StickerModel("Reaksiku", "Rp 5.000", R.drawable.my_frenemy___as_10_____3__, "Meme"))
            defaults.add(StickerModel("Grrrrr", "Rp 5.000", R.drawable.how_to_create_goofy_drawings__fun_and_easy_step_by_step_guide___fascinate_names, "Meme"))
            defaults.add(StickerModel("Mikir kids", "Rp 5.000", R.drawable.thinkbruh, "Meme"))
            defaults.add(StickerModel("Wleee", "Rp 5.000", R.drawable.rockkkk, "Meme"))
            
            // CHARACTER (6)
            defaults.add(StickerModel("Belle", "Rp 5.000", R.drawable.design_a_bedroom_and_we_ll_tell_you_which_disney_princess_you_re_most_like, "Character"))
            defaults.add(StickerModel("Aang", "Rp 5.000", R.drawable._3_unpopular__, "Character"))
            defaults.add(StickerModel("Venellope", "Rp 5.000", R.drawable.wreck_it_ralph_vanellope_von_schweetz, "Character"))
            defaults.add(StickerModel("Minion", "Rp 5.000", R.drawable.minionsss, "Character"))
            defaults.add(StickerModel("Olaf", "Rp 5.000", R.drawable.moviegoers_love__, "Character"))
            defaults.add(StickerModel("Moana", "Rp 5.000", R.drawable.hey_maui__it_s_been_a_little_while__i_i_i_don_t_know_where_you_are_but__takes_a_deep_breath__i_could_really_use_your_help__, "Character"))
            
            // OTHERS (2)
            defaults.add(StickerModel("Snoopy Sticker", "Rp 7.000", R.drawable.snoopy, "Animal"))
            defaults.add(StickerModel("Fin & Jake", "Rp 7.000", R.drawable.fin___jake_, "Character"))

            editor.putInt("INV_COUNT", defaults.size)
            defaults.forEachIndexed { index, s ->
                val i = index + 1
                editor.putString("INV_NAME_$i", s.name)
                editor.putString("INV_PRICE_$i", s.price)
                editor.putInt("INV_IMAGE_$i", s.image)
                editor.putString("INV_CAT_$i", s.category)
            }
            editor.putBoolean("IS_INITIALIZED", true)
            editor.apply()
        }
    }

    private fun processLogin(usernameInput: String, passInput: String) {
        val loginData = LoginRequest(usernameInput, passInput)
        val sessionManager = SessionManager(this)

        ApiConfig.getApiService().login(loginData).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val hasil = response.body()
                    if (hasil?.status == "success") {
                        initInventoryIfNeeded() // Isi data stiker pas login sukses
                        sessionManager.saveSession(hasil.userId ?: 0, usernameInput, usernameInput, "user")
                        startActivity(Intent(this@MainActivity, Homepage::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, hasil?.message ?: "Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Koneksi Gagal", Toast.LENGTH_SHORT).show()
            }
        })
    }
}