package com.example.studentrecordapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etRegUsername = findViewById<EditText>(R.id.etRegUsername)
        val etRegPassword = findViewById<EditText>(R.id.etRegPassword)
        val etRegConfirmPassword = findViewById<EditText>(R.id.etRegConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvBackToLogin = findViewById<TextView>(R.id.tvBackToLogin)

        btnRegister.setOnClickListener {
            val username = etRegUsername.text.toString()
            val password = etRegPassword.text.toString()
            val confirmPassword = etRegConfirmPassword.text.toString()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (AccountManager.register(Account(username, password))) {
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
            }
        }

        tvBackToLogin.setOnClickListener {
            finish()
        }
    }
}