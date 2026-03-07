package com.example.stickit_app

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("StickItSession", Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveSession(userId: Int, name: String, email: String, role: String = "user") {
        editor.putBoolean("isLoggedIn", true)
        editor.putInt("user_id", userId)
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("role", role)
        editor.apply()
    }

    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean("isLoggedIn", false)
    fun getName(): String? = sharedPreferences.getString("name", "User")
    fun getEmail(): String? = sharedPreferences.getString("email", "email@gmail.com")
    fun getRole(): String? = sharedPreferences.getString("role", "user")

    fun logout() {
        editor.clear()
        editor.apply()
    }
}