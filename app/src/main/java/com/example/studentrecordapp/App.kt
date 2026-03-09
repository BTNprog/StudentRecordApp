package com.example.studentrecordapp

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        loadData()
        
        // Add sample students if none exist
        StudentManager.addSampleStudents()
        
        // Apply saved theme
        val sharedPreferences = getSharedPreferences("StudentRecordPrefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun saveData() {
        val sharedPreferences = getSharedPreferences("StudentRecordPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        
        val studentsJson = gson.toJson(StudentManager.students)
        val accountsJson = gson.toJson(AccountManager.accounts)
        
        editor.putString("students", studentsJson)
        editor.putString("accounts", accountsJson)
        editor.putBoolean("isLoggedIn", AccountManager.isLoggedIn)
        editor.putString("currentUsername", AccountManager.currentUsername)
        editor.apply()
    }

    fun setTheme(isDarkMode: Boolean) {
        val sharedPreferences = getSharedPreferences("StudentRecordPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isDarkMode", isDarkMode).apply()
        
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("StudentRecordPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        
        val studentsJson = sharedPreferences.getString("students", null)
        if (studentsJson != null) {
            val type = object : TypeToken<MutableList<Student>>() {}.type
            val loadedStudents: MutableList<Student> = gson.fromJson(studentsJson, type)
            StudentManager.students.clear()
            StudentManager.students.addAll(loadedStudents)
        }
        
        val accountsJson = sharedPreferences.getString("accounts", null)
        if (accountsJson != null) {
            val type = object : TypeToken<MutableList<Account>>() {}.type
            val loadedAccounts: MutableList<Account> = gson.fromJson(accountsJson, type)
            AccountManager.accounts.clear()
            AccountManager.accounts.addAll(loadedAccounts)
        }

        AccountManager.isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        AccountManager.currentUsername = sharedPreferences.getString("currentUsername", null)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}