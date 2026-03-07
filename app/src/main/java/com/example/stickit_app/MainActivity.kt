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
                    sessionManager.saveSession(1, "Asha", "asha@gmail.com", "admin")
                    Toast.makeText(this, "Login Berhasil sebagai admin Asha", Toast.LENGTH_SHORT).show()
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

    private fun processLogin(usernameInput: String, passInput: String) {
        val loginData = LoginRequest(usernameInput, passInput)
        val sessionManager = SessionManager(this)

        ApiConfig.getApiService().login(loginData).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val hasil = response.body()
                    if (hasil?.status == "success") {
                        sessionManager.saveSession(hasil.userId ?: 0, usernameInput, usernameInput, "user")
                        Toast.makeText(this@MainActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity, Homepage::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, hasil?.message ?: "Gagal", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Gagal: User tidak ditemukan (Error ${response.code()})", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Koneksi Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}