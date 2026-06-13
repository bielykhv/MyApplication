package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import dev.spght.encryptedprefs.EncryptedSharedPreferences
import dev.spght.encryptedprefs.MasterKey
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

actual fun getStorageForToken(): Settings {
    val context: Context by inject(Context::class.java)
    val masterKey = MasterKey(context = context)
    val sharedPreferences: SharedPreferences = EncryptedSharedPreferences(
        context = context,
        fileName = context.packageName+"_encrypted_preferences",
        masterKey = masterKey,
    )
    return SharedPreferencesSettings(sharedPreferences)
}