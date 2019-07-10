package com.e.envirohabit_k.controller

import android.content.Context

class MyPreference(context : Context) {

    val PREFERENCE_NAME = "userPreference"
    val PREFERENCE_EMAIL = "email"
    val PREFERENCE_USERNAME = "username"
    val PREFERENCE_POINTS = "points"

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun setEmail(email : String) {
        val editor = preference.edit()
        editor.putString(PREFERENCE_EMAIL, email)
        editor.apply()
    }
    fun setUsername(username : String) {
        val editor = preference.edit()
        editor.putString(PREFERENCE_USERNAME, username)
        editor.apply()
    }
    fun setPoints(points : Int) {
        val editor = preference.edit()
        editor.putInt(PREFERENCE_POINTS, points)
        editor.apply()
    }

    fun getEmail() : String {
        return preference.getString(PREFERENCE_EMAIL, "not_found").toString()
    }
    fun getUsername() : String {
        return preference.getString(PREFERENCE_USERNAME, "not_found").toString()
    }
    fun getPoints() : String {
        return preference.getInt(PREFERENCE_POINTS, -1).toString()
    }
}