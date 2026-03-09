package com.example.studentrecordapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ReportsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        val tvTotalStudents = findViewById<TextView>(R.id.tvTotalStudents)
        tvTotalStudents.text = StudentManager.students.size.toString()
    }
}