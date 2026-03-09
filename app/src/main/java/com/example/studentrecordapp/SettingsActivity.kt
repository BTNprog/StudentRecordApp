package com.example.studentrecordapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val switchDarkMode = findViewById<SwitchMaterial>(R.id.switchDarkMode)
        
        // Set switch state based on current theme
        val sharedPreferences = getSharedPreferences("StudentRecordPrefs", Context.MODE_PRIVATE)
        switchDarkMode.isChecked = sharedPreferences.getBoolean("isDarkMode", false)

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            App.instance.setTheme(isChecked)
        }

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            AccountManager.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}