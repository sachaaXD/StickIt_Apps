package com.example.stickit_app

import com.google.gson.annotations.SerializedName

// Model untuk Login
data class LoginRequest(
    @SerializedName("name") val name: String, // Kembali ke 'name' sesuai database
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("user_id") val userId: Int?,
    @SerializedName("token") val token: String?
)

// Model untuk Register
data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)