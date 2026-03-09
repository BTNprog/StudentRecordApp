package com.example.studentrecordapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StudentDetailsActivity : AppCompatActivity() {

    private var student: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        student = intent.getSerializableExtra("STUDENT") as? Student

        if (student == null) {
            finish()
            return
        }

        updateUI()

        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            val intent = Intent(this, AddEditStudentActivity::class.java)
            intent.putExtra("STUDENT", student)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            StudentManager.deleteStudent(student!!.id)
            Toast.makeText(this, "Student deleted", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateUI() {
        // Fetch the latest data for the student in case it was edited
        student = StudentManager.students.find { it.id == student?.id }
        
        if (student == null) {
            finish()
            return
        }

        val ivProfile = findViewById<ImageView>(R.id.ivDetailProfileImage)
        student?.profileImageUri?.let {
            try {
                ivProfile.setImageURI(Uri.parse(it))
            } catch (e: Exception) {
                ivProfile.setImageResource(android.R.drawable.ic_menu_camera)
            }
        } ?: run {
            ivProfile.setImageResource(android.R.drawable.ic_menu_camera)
        }

        findViewById<TextView>(R.id.tvDetailName).text = student?.name
        findViewById<TextView>(R.id.tvDetailClass).text = "Class: ${student?.studentClass}"
        findViewById<TextView>(R.id.tvDetailGender).text = "Gender: ${student?.gender}"
        findViewById<TextView>(R.id.tvDetailAge).text = "Age: ${student?.getAge()}"
        findViewById<TextView>(R.id.tvDetailEmail).text = "Email: ${student?.email}"
        findViewById<TextView>(R.id.tvDetailPhone).text = "Phone: ${student?.phoneNumber}"
        findViewById<TextView>(R.id.tvDetailAddress).text = "Address: ${student?.address}"
        findViewById<TextView>(R.id.tvDetailLevel).text = "Level: ${student?.level}"
        findViewById<TextView>(R.id.tvDetailGuardianName).text = "Guardian Name: ${student?.guardianName}"
        findViewById<TextView>(R.id.tvDetailGuardianContact).text = "Guardian Contact: ${student?.guardianContact}"
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }
}