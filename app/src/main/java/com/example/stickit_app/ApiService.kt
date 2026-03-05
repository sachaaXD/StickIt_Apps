package com.example.stickit_app

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login.php")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("register.php")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
}