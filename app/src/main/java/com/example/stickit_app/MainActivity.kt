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
        val isLogout = intent.getBooleanExtra("isLogout", false)

        if (sessionManager.isLoggedIn() && !isLogout) {
            startActivity(Intent(this, Homepage::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val etEmail = findViewById<EditText>(R.id.etUsernameLogin)
        val etPassword = findViewById<EditText>(R.id.etPasswordLogin)
        val btnLogin = findViewById<AppCompatButton>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password harus diisi!", Toast.LENGTH_SHORT).show()
            } else {
                processLogin(email, password)
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

    // ... (Bagian import di atas biarin aja)

    private fun processLogin(userNameInput: String, passInput: String) {
        // Buat object request sesuai UserModel lo yang baru
        val loginData = LoginRequest(userNameInput, passInput)

        // Panggil apiService.login() -- Retrofit otomatis bakal pakai POST
        ApiConfig.getApiService().login(loginData).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // Jika berhasil tembus (Log 200 di VS Code)
                if (response.isSuccessful) {
                    val hasil = response.body()
                    if (hasil?.status == "success") {
                        Toast.makeText(this@MainActivity, "Berhasil!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity, Homepage::class.java))
                        finish()
                    } else {
                        // Ini muncul kalau password salah (Log 401 di VS Code)
                        Toast.makeText(
                            this@MainActivity,
                            hasil?.message ?: "Gagal",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Ini muncul kalau IP salah atau Server mati
                Toast.makeText(this@MainActivity, "Koneksi Gagal: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}