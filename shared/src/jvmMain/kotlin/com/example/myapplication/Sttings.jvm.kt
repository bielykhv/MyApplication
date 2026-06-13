package com.example.myapplication

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

actual fun getStorageForToken(): Settings {
    val prefsNode = Preferences.userRoot().node("com.app.tokens")
    return PreferencesSettings(prefsNode)
}