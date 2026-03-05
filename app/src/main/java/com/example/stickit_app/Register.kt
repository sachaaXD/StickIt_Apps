package com.example.stickit_app

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

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val etFullname = findViewById<EditText>(R.id.etUsernameRegister)
        val etEmail = findViewById<EditText>(R.id.etEmailRegister)
        val etPassword = findViewById<EditText>(R.id.etPasswordRegister)
        val btnRegister = findViewById<AppCompatButton>(R.id.btnRegister)
        val tvLogin = findViewById<TextView>(R.id.tvRegister)

        btnRegister.setOnClickListener {
            val name = etFullname.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua data harus diisi!", Toast.LENGTH_SHORT).show()
            } else {
                processRegister(name, email, password)
            }
        }

        tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun processRegister(name: String, email: String, pass: String) {
        // Sekarang constructor RegisterRequest menerima 3 argumen: name, email, pass
        val request = RegisterRequest(name, email, pass)

        ApiConfig.getApiService().register(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        Toast.makeText(this@Register, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@Register, body?.message ?: "Gagal", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Menangani error seperti 409 (Email sudah ada) atau 400 (Bad Request)
                    Toast.makeText(this@Register, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@Register, "ERROR: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}