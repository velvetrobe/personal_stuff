package com.example.personal_stuff.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("personal_stuff_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val USER_ID = "user_id"
        private const val USER_NAME = "user_name"
        private const val USER_EMAIL = "user_email"
        private const val USER_BIRTH_DATE = "user_birth_date"
    }

    fun saveUser(user: com.example.personal_stuff.models.User) {
        prefs.edit()
            .putInt(USER_ID, user.id)
            .putString(USER_NAME, user.name)
            .putString(USER_EMAIL, user.email)
            .putString(USER_BIRTH_DATE, user.birthDate)
            .apply()
    }

    fun getUser(): com.example.personal_stuff.models.User? {
        val id = prefs.getInt(USER_ID, -1)
        if (id == -1) return null // No user saved
        return com.example.personal_stuff.models.User(
            id = id,
            name = prefs.getString(USER_NAME, "") ?: "",
            email = prefs.getString(USER_EMAIL, "") ?: "",
            birthDate = prefs.getString(USER_BIRTH_DATE, "") ?: ""
        )
    }

    fun clearUser() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.contains(USER_ID)
    }
}