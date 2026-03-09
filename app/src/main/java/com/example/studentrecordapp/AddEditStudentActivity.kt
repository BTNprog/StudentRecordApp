package com.example.studentrecordapp

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class AddEditStudentActivity : AppCompatActivity() {

    private var student: Student? = null
    private var selectedDateOfBirth: Long = 0
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            try {
                contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                selectedImageUri = it
                findViewById<ImageView>(R.id.ivProfileImage).setImageURI(it)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Suppress("DEPRECATION", "UNCHECKED_CAST")
    private fun <T : Serializable?> Intent.getSerializable(key: String, clazz: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.getSerializableExtra(key, clazz)
        else
            this.getSerializableExtra(key) as? T
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_student)

        student = intent.getSerializable("STUDENT", Student::class.java)

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val etName = findViewById<EditText>(R.id.etName)
        val etClass = findViewById<EditText>(R.id.etClass)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhoneNumber = findViewById<EditText>(R.id.etPhoneNumber)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val rgLevel = findViewById<RadioGroup>(R.id.rgLevel)
        val etGuardianName = findViewById<EditText>(R.id.etGuardianName)
        val etGuardianContact = findViewById<EditText>(R.id.etGuardianContact)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val btnDatePicker = findViewById<Button>(R.id.btnDatePicker)
        val tvSelectedDOB = findViewById<TextView>(R.id.tvSelectedDOB)
        val ivProfileImage = findViewById<ImageView>(R.id.ivProfileImage)
        val btnPickImage = findViewById<Button>(R.id.btnPickImage)
        val btnSave = findViewById<Button>(R.id.btnSave)

        if (student != null) {
            tvTitle.text = "Edit Student"
            etName.setText(student!!.name)
            etClass.setText(student!!.studentClass)
            etEmail.setText(student!!.email)
            etPhoneNumber.setText(student!!.phoneNumber)
            etAddress.setText(student!!.address)
            
            when (student!!.level) {
                "Level 1" -> findViewById<RadioButton>(R.id.rbLevel1).isChecked = true
                "Level 2" -> findViewById<RadioButton>(R.id.rbLevel2).isChecked = true
                "Level 3" -> findViewById<RadioButton>(R.id.rbLevel3).isChecked = true
            }

            etGuardianName.setText(student!!.guardianName)
            etGuardianContact.setText(student!!.guardianContact)
            selectedDateOfBirth = student!!.dateOfBirth
            if (selectedDateOfBirth != 0L) {
                tvSelectedDOB.text = SimpleDateFormat("'DOB: 'dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDateOfBirth))
            }
            
            student!!.profileImageUri?.let {
                try {
                    selectedImageUri = it.toUri()
                    ivProfileImage.setImageURI(selectedImageUri)
                } catch (e: Exception) {
                    ivProfileImage.setImageResource(android.R.drawable.ic_menu_camera)
                }
            }

            when (student!!.gender) {
                "Male" -> findViewById<RadioButton>(R.id.rbMale).isChecked = true
                "Female" -> findViewById<RadioButton>(R.id.rbFemale).isChecked = true
                "Other" -> findViewById<RadioButton>(R.id.rbOther).isChecked = true
            }
        } else {
            tvTitle.text = "Add Student"
        }

        btnPickImage.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        btnDatePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (selectedDateOfBirth != 0L) {
                calendar.timeInMillis = selectedDateOfBirth
            }
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, y, m, d ->
                val newCalendar = Calendar.getInstance()
                newCalendar.set(y, m, d)
                selectedDateOfBirth = newCalendar.timeInMillis
                tvSelectedDOB.text = SimpleDateFormat("'DOB: 'dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDateOfBirth))
            }, year, month, day).show()
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val sClass = etClass.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhoneNumber.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val gName = etGuardianName.text.toString().trim()
            val gContact = etGuardianContact.text.toString().trim()
            
            val selectedLevelId = rgLevel.checkedRadioButtonId
            val level = if (selectedLevelId != -1) findViewById<RadioButton>(selectedLevelId).text.toString() else ""

            val selectedGenderId = rgGender.checkedRadioButtonId
            if (name.isEmpty() || sClass.isEmpty()) {
                Toast.makeText(this, "Name and Class are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender = if (selectedGenderId != -1) findViewById<RadioButton>(selectedGenderId).text.toString() else ""

            val updatedOrNewStudent = Student(
                id = student?.id ?: UUID.randomUUID().toString(),
                name = name,
                studentClass = sClass,
                email = email,
                level = level,
                gender = gender,
                dateOfBirth = selectedDateOfBirth,
                phoneNumber = phone,
                address = address,
                guardianName = gName,
                guardianContact = gContact,
                profileImageUri = selectedImageUri?.toString()
            )

            if (student != null) {
                StudentManager.updateStudent(updatedOrNewStudent)
            } else {
                StudentManager.addStudent(updatedOrNewStudent)
            }
            finish()
        }
    }
}