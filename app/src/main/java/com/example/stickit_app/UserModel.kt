package com.example.stickit_app

import com.google.gson.annotations.SerializedName

// Login Models
data class LoginRequest(
    @SerializedName("name") val name: String, // Harus 'name' agar sinkron dengan login.php
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("user_id") val userId: Int?, // Menangkap 'user_id' dari PHP
    @SerializedName("token") val token: String?
)

// Register Models
data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)