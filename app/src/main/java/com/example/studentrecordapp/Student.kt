package com.example.studentrecordapp

import java.io.Serializable
import java.util.Calendar

data class Student(
    val id: String = "",
    val name: String = "",
    val studentClass: String = "",
    val email: String = "",
    val gender: String = "",
    val dateOfBirth: Long = 0,
    val phoneNumber: String = "",
    val address: String = "",
    val guardianName: String = "",
    val guardianContact: String = "",
    val profileImageUri: String? = null,
    val level: String = ""
) : Serializable {
    
    fun getAge(): Int {
        if (dateOfBirth == 0L) return 0
        val dob = Calendar.getInstance()
        dob.timeInMillis = dateOfBirth
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return if (age < 0) 0 else age
    }
}