package com.example.doform


import android.content.Context

object ThemeUtils {
    const val PREFS_NAME = "theme_prefs"
    const val KEY_THEME = "current_theme"

    fun saveThemePreference(context: Context, isDarkMode: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(KEY_THEME, isDarkMode).apply()
    }

    fun isDarkMode(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_THEME, false)
    }
}
